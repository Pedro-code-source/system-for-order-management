import styles from "./TableCard.module.css";

export default function TableCard({
  numero,
  lugares,
  status,
}) {

  const classe =
    status === "Ocupada"
      ? styles.ocupada
      : status === "Reservada"
      ? styles.reservada
      : styles.disponivel;

  return (
    <div
      className={`${styles.card} ${classe}`}
    >
      <h2>{numero}</h2>

      <p>{lugares} lugares</p>

      <select defaultValue={status}>

        <option>
          Disponível
        </option>

        <option>
          Ocupada
        </option>

        <option>
          Reservada
        </option>

      </select>

    </div>
  );
}