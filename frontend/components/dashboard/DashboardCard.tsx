import styles from "./DashboardCard.module.css";

type Props = {
  title: string;
  value: string;
  label: string;
};

export default function DashboardCard({
  title,
  value,
  label,
}: Props) {
  return (
    <div className={styles.card}>

      <span>
        {title}
      </span>

      <h2>
        {value}
      </h2>

      <p>
        {label}
      </p>

    </div>
  );
}