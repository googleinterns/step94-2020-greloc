
const WEBSITE_URL = "https://8080-b83fc153-d2cf-481d-a321-9342cdf80f21.us-east1.cloudshell.dev";
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