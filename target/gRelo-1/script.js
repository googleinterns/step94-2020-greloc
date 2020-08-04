
const WEBSITE_URL = "https://8080-74f086d4-db1e-49b6-b00a-2fb76ef31083.us-east1.cloudshell.dev";
//const WEBSITE_URL = "https://greloc.uc.r.appspot.com"
    async function signOut () {
        let response = await fetch(WEBSITE_URL + '/signoutapi');
        if (response.ok) {
          let respObject = await response.json();
          console.log(respObject.signOutURL);
          window.location.href = WEBSITE_URL + respObject;
        } else {
          alert("There was an issue processing your request, please try again later");
        }   
      }