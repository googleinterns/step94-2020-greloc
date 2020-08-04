import { WEBSITE_URL } from './constants.js'

/**
 * Checks if the user is logged in, redirects to landing page if not
 */
const authCheck = async function () {
  let response;
  try {
    response = await fetch(WEBSITE_URL + `/userData`);   
  } catch (error) {
    response = null;
  }

  return new Promise((resolve, reject) => {
    if (response.ok) {
      resolve();
    } else {
      reject();
    }
  })
}

/**
 * Attempts to retrieve data about the currently signed in user
 * 
 * @returns Object with UserData if request succeeds. Null if it fails
 */
const fetchUserData = async function () {
  let response = await fetch(WEBSITE_URL + `/userData`);
  if (response.ok) {
    let respData = await response.json();
    return respData;
  } else {
    return null; 
  }
}

/**
 * Creates a cookie to store the information in `data`
 * 
 * @param data: UserData object
 * @returns Object with attributes `id` and `email`
 */
const createUserCookieWithData = function (data) {
  setCookie("id", data.id, 20);
  setCookie("email", data.email, 20);
}

/**
 * Attempts to retrieve data from cookies to populate an object with said data
 * Will return object with empty strings if cookie is not found
 * 
 * @returns Object with attributes `id` and `email`
 */
const getUserObjectFromCookie = function(){
  return {
    id: getCookie("id"),
    email: getCookie("email")
  }
}


// Cookie creation function from https://www.w3schools.com/js/js_cookies.asp 
function setCookie(cname, cvalue, exdays) {
  var d = new Date();
  d.setTime(d.getTime() + (exdays*24*60*60*1000));
  var expires = "expires="+ d.toUTCString();
  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

// Cookie reading function from https://www.w3schools.com/js/js_cookies.asp 
function getCookie(cname) {
  var name = cname + "="
  var decodedCookie = document.cookie
  var ca = decodedCookie.split(';')
  for(var i = 0; i <ca.length; i++) {
      var c = ca[i]
      while (c.charAt(0) == ' ') {
          c = c.substring(1)
      }
      if (c.indexOf(name) == 0) {
          return c.substring(name.length, c.length)
      }
  }
  return ""
}

export { authCheck, fetchUserData, getUserObjectFromCookie, createUserCookieWithData};