"use client"
import React, { useState } from "react";
import styles from "./login.module.css";
import { useRouter } from "next/navigation";

type LoginProps = {
    status?: "success" | "idle";
};



export default function Login({ status }: LoginProps) {
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");
    const router = useRouter();

    async function fazerLogin(
  e: React.FormEvent
) {

  e.preventDefault();

  try {

    const response = await fetch(
      "http://localhost:8080/login",
      {
        method: "POST",

        headers: {
          "Content-Type": "application/json",
        },

        body: JSON.stringify({
          email,
          senha,
        }),
      }
    );

    if(response.ok) {

      const usuario = await response.json();

      console.log(usuario);

      alert("Login realizado!");

      router.push("cliente");

    } else {

      alert("Email ou senha inválidos");
    }

  } catch(error) {

    console.log(error);

    alert("Erro no servidor");
  }
}


            return (
                <div className={styles.wrapper}>
                    <div className={styles.logoBox}>
                        🍽️
                    </div>

                    <div className={styles.header}>
                        <h1>Restaurant System</h1>
                        <p>Sistema Completo de Gerenciamento</p>
                    </div>

                    <div className={styles.container}>
                        <form onSubmit={fazerLogin} className={styles.formLogin}>

                            <div className={styles.entradas}>
                                <label htmlFor="email-login">Email</label>

                                <div className={styles.inputBox}>
                                    <span>✉️</span>

                                    <input
                                        id="email-login"
                                        type="email"
                                        placeholder="seu@email.com"
                                        value={email}
                                        disabled={status === "success"}
                                        onChange={(e) => setEmail(e.target.value)}
                                    />
                                </div>
                            </div>

                            <div className={styles.entradas}>
                                <label htmlFor="senha-login">Senha</label>

                                <div className={styles.inputBox}>
                                    <span>🔒</span>

                                    <input
                                        id="senha-login"
                                        type="password"
                                        placeholder="••••••••"
                                        value={senha}
                                        disabled={status === "success"}
                                        onChange={(e) => setSenha(e.target.value)}
                                    />
                                </div>
                            </div>

                            <button
                                className={styles.submit}
                                disabled={status === "success"}
                            >
                                Entrar
                            </button>

                            
                        </form>
                        <button onClick={() => router.push("cadastro")}>Fazer cadastro</button>
                    </div>

                    <span className={styles.footer}>
                        Sistema de gerenciamento completo para restaurantes
                    </span>
                </div>
            );
        }