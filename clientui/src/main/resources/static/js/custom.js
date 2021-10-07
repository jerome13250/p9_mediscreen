/**
 * This is script to access to diabete assessment with a fetch in patientNotes.html
 */

//URL structure: <protocol>//<hostname>:<port>/<pathname><search><hash>
//remove search and hash from url:
 let url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + window.location.pathname + "/diabeteAssess";
 console.log("url : " + url);
 
 
 function askDiabeteAssess() {
  fetch(url)
  .then(function(res) {
    if (res.ok) {
		return res.text();
    }
  })
  .then(function(value) {
    document
        .getElementById("result-diabeteassess")
        .innerText = value;
  })
  .catch(function(err) {
	let errorMsg = "Une erreur est survenue : " + err.message;
    console.log(errorMsg);
    document
        .getElementById("result-diabeteassess")
        .innerText = errorMsg;
  });
}

document
  .getElementById("ask-diabeteassess")
  .addEventListener("click", askDiabeteAssess);