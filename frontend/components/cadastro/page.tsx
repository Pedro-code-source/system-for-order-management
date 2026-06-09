"use client"
import { useState } from "react";
import styles from "./register.module.css";
import { useRouter } from "next/navigation";

type CadastroProps = {
  status?: "success" | "idle";
};

export default function Cadastro({ status }: CadastroProps) {
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [confirmar, setConfirmar] = useState("");
  const [telefone, setTelefone] = useState("");
  const router = useRouter();
  const endereco = {cep:"a",
              rua:"a",
              numero:"a",
              bairro:"a",
              cidade:"a"
            };

  async function fazerCadastro(e: React.FormEvent){

    e.preventDefault();

    if (senha != confirmar) {
      alert("As senhas não coincidem!");
      return
    }

    try{
      const response = await fetch("http://localhost:8080/clientes",
        {method:"POST",
          headers: {"Content-Type": "application/json",},
          body: JSON.stringify({
            nome, email, telefone, senha, endereco
          }),
        }
      );

      if (response.ok) {
        alert("Cadastro Realizado!");
        router.push("/login")
      } else {
        const erro = await response.text();
        alert(erro)
      }
    }catch(error){
      console.log(error)
      alert("Erro no servidor")
    }
  }

  return (
    <div className={styles.wrapper}>
      <div className={styles.logoBox}>
        🍽️
      </div>

      <div className={styles.header}>
        <h1>Criar Conta</h1>
        <p>Cadastre-se para acessar o sistema</p>
      </div>

      <div className={styles.container}>
        <form onSubmit={fazerCadastro} className={styles.form}>

          <div className={styles.field}>
            <label>Nome</label>

            <div className={styles.inputBox}>
              <span>👤</span>

              <input
                type="text"
                placeholder="Seu nome"
                value={nome}
                disabled={status === "success"}
                onChange={(e) => setNome(e.target.value)}
              />
            </div>
          </div>

          <div className={styles.field}>
            <label>Telefone</label>

            <div className={styles.inputBox}>
              <span>📞</span>

              <input
                type="text"
                placeholder="(XX)XXXXX XXXX"
                value={telefone}
                disabled={status === "success"}
                onChange={(e) => setTelefone(e.target.value)}
              />

            </div>
          </div>

          <div className={styles.field}>
            <label>Email</label>

            <div className={styles.inputBox}>
              <span>✉️</span>

              <input
                type="email"
                placeholder="seu@email.com"
                value={email}
                disabled={status === "success"}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
          </div>

          <div className={styles.field}>
            <label>Senha</label>

            <div className={styles.inputBox}>
              <span>🔒</span>

              <input
                type="password"
                placeholder="••••••••"
                value={senha}
                disabled={status === "success"}
                onChange={(e) => setSenha(e.target.value)}
              />
            </div>
          </div>

          <div className={styles.field}>
            <label>Confirmar senha</label>

            <div className={styles.inputBox}>
              <span>🔐</span>

              <input
                type="password"
                placeholder="Repita a senha"
                value={confirmar}
                disabled={status === "success"}
                onChange={(e) => setConfirmar(e.target.value)}
              />
            </div>
          </div>

          

          <button
            className={styles.submit}
            disabled={status === "success"}
          >
            Criar Conta
          </button>

        </form>
      </div>
    </div>
  );
}