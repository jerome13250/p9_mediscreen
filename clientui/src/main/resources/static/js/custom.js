/**
 * 
 */
 
const elt = document.getElementById('mon-lien');    // On récupère l'élément sur lequel on veut détecter le clic
	elt.addEventListener('click', function() {          // On écoute l'événement click
    elt.innerHTML = "C'est cliqué !";               // On change le contenu de notre élément pour afficher "C'est cliqué !"
});

alert("I'm active");