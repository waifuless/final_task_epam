function setLocaleAndReloadPage(locale){
    document.cookie = `lang=${locale}`;
    location.reload();
}