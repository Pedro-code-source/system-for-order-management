import React, { useState } from "react";
import styles from "./login.module.css"

export default function Login({status}){
    const [email, setEmail] = useState("");
    const [senha, setSenha] = useState("");

    // if (status === "success"){
    //     return <h2>Login realizado com sucesso!</h2>
    // }
    
    return (
        <div className={styles.container}>

            <form className={styles.formLogin} id = 'form-login' action="">

                <h2>Login</h2>

                <div className={styles.entradas}>
                    <label htmlFor="email-login">Email:</label>
                    <input disabled = {status === "success"} id="email-login" type="text" />
                </div>

                <div className={styles.entradas}>
                    <label htmlFor="senha-login">Senha:</label>
                    <input disabled = {status === "success"} id="senha-login" type="text" />
                </div>
               
                <button className={styles.submit} disabled = {status === "success"}>Enviar</button>
            </form>

        </div>
    )

}