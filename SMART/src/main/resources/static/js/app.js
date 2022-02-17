document.addEventListener("DOMContentLoaded", init);

function init() {
    console.log("Page is loaded.");
}

const clientBtn = document.getElementById("clientBtn");
const ressBtn = document.getElementById("ressBtn");
const msgBtn = document.getElementById("msgBtn");

const clt = document.getElementById("divClient");
const res = document.getElementById("divRes");
const msgs = document.getElementById("divMsgs");

clientBtn.addEventListener("click", displayClientTab);
ressBtn.addEventListener("click", displayResTab);
msgBtn.addEventListener("click", displayMsgsTab);

function displayClientTab() {
    clt.style.display = "contents"
    msgs.style.display = "none"
    res.style.display = "none"
}

function displayResTab() {
    clt.style.display = "none"
    msgs.style.display = "none"
    res.style.display = "contents"
}

function displayMsgsTab() {
    clt.style.display = "none"
    msgs.style.display = "contents"
    res.style.display = "none"
}

﻿