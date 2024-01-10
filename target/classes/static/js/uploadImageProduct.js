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
const form = document.querySelector('#form1')
const loading = document.querySelector('.loading')
let oldImageURL = ""
let fileItem;
let fileName;

function getFile(e) {
    oldImageURL = document.querySelector('.imagePreview').src;
    fileItem = e.target.files[0];
    fileName = Date.now() + '-' + fileItem.name;
    let imagePreview = document.querySelector('.imagePreview');
    imagePreview.src = window.URL.createObjectURL(fileItem)
}
fileInp.addEventListener('change', (e) => getFile(e));

function uploadImage() {

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
                let imageField = document.querySelector('input[name="photo"]');
                imageField.setAttribute('value', url);
                form.submit();
            });
        }
    );
}

form.addEventListener('submit', (e) => {
    e.preventDefault();
    loading.classList.add('active')
    if(fileItem !== undefined)
        uploadImage()
    else {
        form.submit();
    }
})

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