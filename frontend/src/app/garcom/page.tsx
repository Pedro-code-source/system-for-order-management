"use client";


import DashboardLayout from "../../../components/dashboard/DashboardLayout";
import TableCard from "../../../components/dashboard/TableCard";
import styles from "./garcom.module.css";

const mesas = [
  { id: 1, lugares: 2, status: "Disponível" },
  { id: 2, lugares: 4, status: "Disponível" },
  { id: 3, lugares: 4, status: "Ocupada" },
  { id: 4, lugares: 6, status: "Disponível" },
  { id: 5, lugares: 2, status: "Reservada" },
  { id: 6, lugares: 8, status: "Disponível" },
];

export default function Garcom() {
  return (
    <div>
      <div className={styles.actions}>

        <button className={styles.primary}>
          Mesas
        </button>

        <button>
          Novo Pedido
        </button>

        <button>
          Pedidos Ativos
        </button>

      </div>

      <div className={styles.legend}>

        <span>
          🟢 Disponível
        </span>

        <span>
          🔴 Ocupada
        </span>

        <span>
          🟡 Reservada
        </span>

      </div>

      <h2>Status das Mesas</h2>

      <div className={styles.grid}>

        {mesas.map((mesa) => (
          <TableCard
            key={mesa.id}
            numero={mesa.id}
            lugares={mesa.lugares}
            status={mesa.status}
          />
        ))}

      </div>
</div>
  );
}