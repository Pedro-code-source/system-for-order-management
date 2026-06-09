import styles from "./ProductCard.module.css";

export default function ProductCard() {
  return (
    <div className={styles.card}>

      <img
        src="/pizza.jpg"
        alt=""
      />

      <div className={styles.body}>

        <h3>Pizza Margherita</h3>

        <span>Molho de tomate e mussarela</span>

        <strong>R$ 35,90</strong>

        <button>
          + Adicionar
        </button>

      </div>

    </div>
  );
}