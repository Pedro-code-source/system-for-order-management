"use client"

import ClientLayout from "../../components/layout/ClientLayout";
import DashboardLayout from "../../components/dashboard/DashboardLayout";
import DashboardCard from "../../components/dashboard/DashboardCard";
import Login from "../../components/login/page";
import Cadastro from "../../components/cadastro/page";

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
    <Login></Login>
  );
}
