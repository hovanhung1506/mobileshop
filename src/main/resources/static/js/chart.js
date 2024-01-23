const revenueChart = document.getElementById('revenueChart');
const brandChart = document.getElementById('brandChart');
let chart1 = null;
let chart2 = null;
const year = document.getElementById('year');
const brand = document.getElementById('brand');
year.addEventListener('change', (e) => {
    const selectedIndex = year.selectedIndex;
    const selectedOptionText = year.options[selectedIndex].value;
    fetchRevenue(selectedOptionText)
});

brand.addEventListener('change', (e) => {
    const selectedIndex = brand.selectedIndex;
    const selectedOptionText = brand.options[selectedIndex].value;
    fetchBrandChart(selectedOptionText)
})

function createBarChart(value) {

    const data = {
        labels: [
            'Tháng 1',
            'Tháng 2',
            'Tháng 3',
            'Tháng 4',
            'Tháng 5',
            'Tháng 6',
            'Tháng 7',
            'Tháng 8',
            'Tháng 9',
            'Tháng 10',
            'Tháng 11',
            'Tháng 12',
        ],
        datasets: [
            {
                label: 'Doanh thu',
                data: value,
                backgroundColor: [
                    'rgba(255, 99, 132)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)',
                    'rgba(255, 99, 132)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)',
                ],
            },
        ],
    };

    const options = {
        responsive: true,
        plugins: {
            tooltip: {
                callbacks: {
                    label: function (context) {
                        let label = '';
                        if (context.parsed.y !== null) {
                            label += new Intl.NumberFormat('vn-VN', {style: 'currency', currency: 'VND'}).format(
                                context.parsed.y
                            );
                        }
                        return label;
                    },
                },
            },
        },
    };

    const config = {
        type: 'bar',
        data: data,
        options: options,
    };
    if (chart1 != null) {
        chart1.destroy();
    }
    chart1 = new Chart(revenueChart, config);
}

function fetchRevenue(year = new Date().getFullYear()) {
    // const loading = document.querySelector('.loading')
    // loading.classList.add('active')
    $.ajax({
        type: "POST",
        url: 'http://localhost:8080/admin/statistic/revenue',
        data: {
            year
        },
        success: function (data) {
            let value = []
            for (let i of data.data) {
                value.push(i)
            }
            // loading.classList.remove('active')
            createBarChart(value)
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });

}

fetchRevenue()

function createPolar(labels, value) {
    const data = {
        labels: labels,
        datasets: [
            {
                label: '',
                data: value,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.5)',
                    'rgba(54, 162, 235, 0.5)',
                    'rgba(255, 206, 86, 0.5)',
                    'rgba(75, 192, 192, 0.5)',
                    'rgba(153, 102, 255, 0.5)',
                    'rgba(255, 159, 64, 0.5)',
                ]
            }
        ]
    };

    const config = {
        type: 'doughnut',
        data: data,
        options: {
            responsive : true,
            plugins: {
                datalabels: {
                    formatter: ((value, ctx) => {
                        let sum = 0;
                        let dataArr = ctx.chart.data.datasets[0].data;
                        dataArr.map(data => {
                            sum += data;
                        });
                        let percentage = (value*100 / sum).toFixed(2)+"%";
                        return percentage;
                    }),
                    color: '#fff',
                },
                tooltip: {
                    callbacks: {
                        label: function (context) {
                            console.log(context)
                            let label = '';
                            if (context.parsed !== null) {
                                label += new Intl.NumberFormat('vn-VN', {style: 'currency', currency: 'VND'}).format(
                                    context.parsed
                                );
                            }
                            let dataArr = context.dataset.data;
                            let sum = 0;
                            dataArr.map(data => {
                                sum += data;
                            });
                            let percentage = (context.parsed * 100 / sum).toFixed(2)+"%";
                            label += ' - ' + percentage;
                            return label;
                        },
                    },
                },
            }
        },
    };

    if (chart2 != null) {
        chart2.destroy();
    }
    chart2 = new Chart(brandChart, config);

}

function fetchBrandChart(year = new Date().getFullYear()) {
    $.ajax({
        type: "POST",
        url: 'http://localhost:8080/admin/statistic/brand',
        data: {
            year
        },
        success: function (data) {
            let value = []
            let labels = []
            for (let i of data.data) {
                labels.push(i.name)
                value.push(i.revenue)
            }
            createPolar(labels, value)
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}

fetchBrandChart()