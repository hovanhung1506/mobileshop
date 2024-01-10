package com.example.mobileshop.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class PaymentOS {

    private final String clientId;
    private final String apiKey;
    private final String checksumKey;

    public PaymentOS(String clientId, String apiKey, String checksumKey) {
        this.clientId = clientId;
        this.apiKey = apiKey;
        this.checksumKey = checksumKey;
    }

    private static String generateHmacSHA256(String dataStr, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] hmacBytes = sha256Hmac.doFinal(dataStr.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexStringBuilder = new StringBuilder();
        byte[] var6 = hmacBytes;
        int var7 = hmacBytes.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            byte b = var6[var8];
            hexStringBuilder.append(String.format("%02x", b));
        }

        return hexStringBuilder.toString();
    }

    public static String createSignatureOfPaymentRequest(PaymentOSData data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        int amount = data.getAmount();
        String cancelUrl = data.getCancelUrl();
        String description = data.getDescription();
        String orderCode = Integer.toString(data.getOrderCode());
        String returnUrl = data.getReturnUrl();
        String dataStr = "amount=" + amount + "&cancelUrl=" + cancelUrl + "&description=" + description + "&orderCode=" + orderCode + "&returnUrl=" + returnUrl;
        return generateHmacSHA256(dataStr, key);
    }

    public JsonNode createPaymentLink(PaymentOSData paymentData) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String url = "https://api-merchant.payos.vn/v2/payment-requests";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        paymentData.setSignature(createSignatureOfPaymentRequest(paymentData, this.checksumKey));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("Charset", "UTF-8");
        httpPost.setHeader("x-client-id", this.clientId);
        httpPost.setHeader("x-api-key", this.apiKey);
        String paymentDataJson = objectMapper.writeValueAsString(paymentData);
        httpPost.setEntity(new StringEntity(paymentDataJson, StandardCharsets.UTF_8));
        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if(entity == null) {
            response.close();
            client.close();
            throw new Exception("Call api failed!");
        } else {
            String responseData = EntityUtils.toString(entity);
            response.close();
            client.close();
            JsonNode res = objectMapper.readTree(responseData);
            if(!Objects.equals(res.get("code").asText(), "00")){
                throw new Exception(res.get("code").asText() + " - " + res.get("desc").asText());
            }else {
                String paymentLinkResSignature = createSignatureFromObj(res.get("data"), this.checksumKey);
                if(!paymentLinkResSignature.equals(res.get("signature").asText())) {
                    throw new Exception("The data is unreliable because the signature of the response does not match the signature of the data");
                }else {
                    return res.get("data");
                }
            }
        }
    }

    public PaymentOSData getPaymentData(Order order) {
        final String description = "Thanh toan don hang: " + order.getId();
        final String returnUrl = "http://localhost:8080/checkout/payment";
        final String cancelUrl = "http://localhost:8080/checkout/payment";
        final int amount = 2000;
        int orderCode = order.getId().intValue();
        return new PaymentOSData(orderCode, amount, description, cancelUrl, returnUrl);
    }

    public static String createSignatureFromObj(JsonNode data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        JsonNode sortedDataByKey = sortObjDataByKey(data);
        String dataQueryStr = convertObjToQueryStr(sortedDataByKey);
        return generateHmacSHA256(dataQueryStr, key);
    }

    private static JsonNode sortObjDataByKey(JsonNode object) {
        if (!object.isObject()) {
            return object;
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode orderedObject = objectMapper.createObjectNode();
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = object.fields();
            TreeMap<String, JsonNode> sortedMap = new TreeMap();

            while(fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = (Map.Entry)fieldsIterator.next();
                sortedMap.put(field.getKey(), field.getValue());
            }

            sortedMap.forEach(orderedObject::set);
            return orderedObject;
        }
    }

    private static String convertObjToQueryStr(JsonNode object) {
        StringBuilder stringBuilder = new StringBuilder();
        object.fields().forEachRemaining((entry) -> {
            String key = (String)entry.getKey();
            JsonNode value = (JsonNode)entry.getValue();
            String valueAsString;
            if (!value.isNull() && !Objects.equals(value.asText(), "null")) {
                if (value.isArray()) {
                    List<JsonNode> valueAsStringList = new ArrayList();
                    Iterator var6 = value.iterator();

                    while(var6.hasNext()) {
                        JsonNode item = (JsonNode)var6.next();
                        valueAsStringList.add(sortObjDataByKey(item));
                    }

                    valueAsString = String.valueOf(valueAsStringList);
                } else if (value.isTextual()) {
                    valueAsString = value.asText();
                } else {
                    valueAsString = value.toString();
                }
            } else {
                valueAsString = "";
            }

            if (!stringBuilder.toString().isEmpty()) {
                stringBuilder.append('&');
            }

            stringBuilder.append(key).append('=').append(valueAsString);
        });
        return stringBuilder.toString();
    }

}
