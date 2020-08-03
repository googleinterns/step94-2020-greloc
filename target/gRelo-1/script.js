

    async function signOut () {
        let response = await fetch(WEBSITE_URL + '/signoutapi');
        if (response.ok) {
          let respObject = await response.json();
          location.href = respObject.authURL;
        } else {
          alert("There was an issue processing your request, please try again later");
        }   
      }