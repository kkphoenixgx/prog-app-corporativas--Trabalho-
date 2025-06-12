const btnMenuClose = document.querySelector("#btn-close-menu");
const btnHamburguerMenu = document.querySelector("#btn-hamburguer-menu");
const containerMenu = document.querySelector("#container-menu");

let containerMenuState = false;

btnHamburguerMenu.addEventListener("click", () => {
  containerMenuState = !containerMenuState;
  containerMenu.style.display = containerMenuState ? "block" : "none";
});

btnMenuClose.addEventListener("click", () => {
  containerMenuState = false;
  containerMenu.style.display = "none";
});