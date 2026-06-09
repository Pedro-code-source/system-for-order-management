import styles from "./DashboardLayout.module.css";

type Props = {
  title: string;
  subtitle: string;
  children: React.ReactNode;
};

export default function DashboardLayout({
  title,
  subtitle,
  children,
}: Props) {
  return (
    <div className={styles.page}>

      <header className={styles.header}>

        <div className={styles.titleArea}>

          <div className={styles.icon}>
            🍽️
          </div>

          <div>
            <h1>{title}</h1>
            <p>{subtitle}</p>
          </div>

        </div>

        <button className={styles.logout}>
          Sair
        </button>

      </header>

      <nav className={styles.nav}>

        <button className={styles.active}>
          Visão Geral
        </button>

        <button>Cardápio</button>

        <button>Mesas</button>

        <button>Reservas</button>

        <button>Pedidos</button>

      </nav>

      <main className={styles.content}>
        {children}
      </main>

    </div>
  );
}