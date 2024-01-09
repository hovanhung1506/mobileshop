const firebaseConfig = {
    apiKey: 'AIzaSyBFFHAisyh6TnRUL9hlNtlTqhX_CRT5nVg',
    authDomain: 'mobileshop-e120b.firebaseapp.com',
    projectId: 'mobileshop-e120b',
    storageBucket: 'mobileshop-e120b.appspot.com',
    messagingSenderId: '62778904766',
    appId: '1:62778904766:web:e51524adcb68f1bb070eef',
    measurementId: 'G-LBJ3GWQ5F4',
};

firebase.initializeApp(firebaseConfig);

const fileInp = document.querySelector('#fileInp')
const saveImage = document.querySelector('.saveImage')
let oldImageURL
let fileItem;
let fileName;

function getFile(e) {
    oldImageURL = document.querySelector('.imagePreview').src;
    fileItem = e.target.files[0];
    fileName = Date.now() + '-' + fileItem.name;
    let imagePreview = document.querySelector('.imagePreview');
    imagePreview.src = window.URL.createObjectURL(fileItem)
    const btnSaveImage = document.querySelector('.details .content .avatar button');
    btnSaveImage.classList.remove('not-allowed');
}

fileInp.addEventListener('change', (e) => getFile(e));
function uploadImage() {
    if (fileItem == null) {
        console.log('image not found');
        return;
    }
    const loading = document.querySelector('.loading')
    loading.classList.add('active')
    if(oldImageURL.startsWith('https://firebasestorage.googleapis.com')) {
        deleteImage(oldImageURL)
    }

    const storageRef = firebase.storage().ref('images/' + fileName);
    const uploadTask = storageRef.put(fileItem);

    uploadTask.on(
        'state_changed',
        (snapshot) => {},
        (error) => { console.log('Error is', error); },
        () => {
            uploadTask.snapshot.ref.getDownloadURL().then((url) => {
                let imagePreview = document.querySelector('.imagePreview');
                imagePreview.setAttribute('src', url);
                saveImageAPI(url)
                loading.classList.remove('active')
            });
        }
    );
}

saveImage.addEventListener('click', () => uploadImage())

function deleteImage(urlImage) {
    const desertRef = firebase.storage().refFromURL(urlImage);
    desertRef
        .delete()
        .then(() => {
            console.log('Delete success');
        })
        .catch((error) => {
            console.log('Delete failed: ', error);
        });
}

function saveImageAPI(url) {
    $.ajax({
        url: 'http://localhost:8080/user/change-image',
        type: 'POST',
        data: {
            photo: url
        },
        success: function (data) {
            if(data.status === "200") {
                document.querySelector('.avatar-small img').setAttribute('src', url);
                showToast(data.message)
            }
        },
        error: function (err) {
            console.log(err)
        }
    })
}
