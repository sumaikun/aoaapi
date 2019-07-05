function encode_utf8(s) {
  return unescape(encodeURIComponent(s));
}

function decode_utf8(s) {

  if(UTF8_ON_DEPLOY)
  {
    return decodeURIComponent(escape(s));
  }
  return s;
}



const LOCAL_MODE = true;
const BASE_URL = window.location.href.indexOf('localhost') > 0 && LOCAL_MODE ? 'http://localhost:8080' : 'http://sac.aoacolombia.com:8080/aoaapi/';
//const BASE_URL = 'http://localhost:8080';
const UTF8_ON_DEPLOY = window.location.href.indexOf('aoaclientes.appspot') > 0  ? true : false;

console.log(window.location.href);

console.log(UTF8_ON_DEPLOY);
