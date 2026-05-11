import React from "react";
import styles from "./register.module.css"

export default function Register({status}){
    return (
        <div className={styles.containerRegister}>
      <form className={styles.formRegister}>
        <h2>Cadastro</h2>

        <div className={styles.entradas}>
          <label>Nome</label>
          <input disabled = {status === "success"} type="text" placeholder="Seu nome completo" required />
        </div>

        <div className={styles.entradas}>
          <label>Email</label>
          <input disabled = {status === "success"} type="email" placeholder="seu@email.com" required />
        </div>

        <div className={styles.entradas}>
          <label>Telefone</label>
          <input disabled = {status === "success"} type="tel" placeholder="(83) 99999-9999" required />
        </div>

        <div className={styles.entradas}>
          <label>Endereço</label>
          <input disabled = {status === "success"} type="text" placeholder="Rua, número, bairro..." required />
        </div>

        <div className={styles.entradas}>
          <label>Senha</label>
          <input disabled = {status === "success"} type="password" placeholder="••••••••" required />
        </div>

        <button disabled = {status === "success"} className={styles.submit} type="submit">Cadastrar</button>
      </form>
    </div>
    )
}