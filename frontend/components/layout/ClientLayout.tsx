import styles from "./ClienteLayout.module.css";

export default function ClientLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className={styles.page}>

      <header className={styles.header}>

        <div className={styles.brand}>
          <div className={styles.logo}>🍽️</div>

          <div>
            <h2>Área do Cliente</h2>
            <span>Olá, usuário</span>
          </div>
        </div>

        <button className={styles.exit}>
          Sair
        </button>

      </header>

      <nav className={styles.tabs}>
        <button className={styles.active}>
          Cardápio
        </button>

        <button>Carrinho</button>

        <button>Minhas Reservas</button>

        <button>Pedidos</button>
      </nav>

      <main>
        {children}
      </main>

    </div>
  );
}