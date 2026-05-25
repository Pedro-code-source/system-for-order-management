"use client"
import { useState, useEffect } from "react";
import Login from "../../components/login/page";
import Register from "../../components/cadastro/page";

export default function Home() {
  // const data = fetch("http://localhost:8080/clientes")
  // const [estado, setEstado] = useState("login");

  // useEffect(() => {
  //   async function carregar() {
  //     const response = await fetch("http://localhost:8080/clientes");
  
  //     const data = await response.json();
  //     data.forEach(e => {
  //       console.log(e.nome);
  //     });
  //   }

  //   carregar();
  // }, []);
  return (
    <div>

      <Login status={"idle"}/>
      <Register status={"idle"} />
    </div>
  );
}
