"use client";

import { useMemo, useState } from "react";

import styles from "./cliente.module.css";

import {
  ShoppingCart,
  Search,
  Star,
  Plus,
  Clock3,
} from "lucide-react";

const produtos = [
  {
    id: 1,
    nome: "Hambúrguer Artesanal",
    descricao: "Pão brioche, carne 180g e cheddar",
    preco: 32.9,
    categoria: "Burgers",
    imagem:
      "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=1200&auto=format&fit=crop",
  },

  {
    id: 2,
    nome: "Pizza Calabresa",
    descricao: "Molho especial e borda recheada",
    preco: 54.9,
    categoria: "Pizzas",
    imagem:
      "https://images.unsplash.com/photo-1513104890138-7c749659a591?q=80&w=1200&auto=format&fit=crop",
  },

  {
    id: 3,
    nome: "Sushi Premium",
    descricao: "Combo com 24 peças especiais",
    preco: 79.9,
    categoria: "Sushi",
    imagem:
      "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?q=80&w=1200&auto=format&fit=crop",
  },

  {
    id: 4,
    nome: "Refrigerante",
    descricao: "Lata 350ml gelada",
    preco: 7.9,
    categoria: "Bebidas",
    imagem:
      "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?q=80&w=1200&auto=format&fit=crop",
  },
];

const mesas = [
  {
    id: 1,
    numero: 1,
    capacidade: 4,
    status: "LIVRE"
  },
  {
    id: 2,
    numero: 2,
    capacidade: 2,
    status: "LIVRE"
  },
  {
    id: 3,
    numero: 3,
    capacidade: 8,
    status: "LIVRE"
  }
];

export default function ClientePage() {
  const [categoriaSelecionada, setCategoriaSelecionada] =
    useState("Todos");

  const [busca, setBusca] = useState("");

  const [carrinho, setCarrinho] = useState<number[]>([]);

  const [abrirCarrinho, setAbrirCarrinho] =
    useState(false);

  const [itensCarrinho, setItensCarrinho] =
    useState<typeof produtos>([]);

  const [cardapio, setCardapio] = useState(true);

  const [reservaMesa, setReservaMesa] = useState(false);

  const [habilitarCarrinho, setHabilitarCarrinho] = useState(true);

  const [mesaSelecionada, setMesaSelecionada] =
  useState(null);




  function adicionarCarrinho(id: number) {
    const produto = produtos.find(
      (p) => p.id === id
    );

    if (!produto) return;

    setItensCarrinho((prev) => [...prev, produto]);
  }

  const produtosFiltrados = useMemo(() => {
    return produtos.filter((produto) => {
      const bateCategoria =
        categoriaSelecionada === "Todos" ||
        produto.categoria === categoriaSelecionada;

      const bateBusca =
        produto.nome
          .toLowerCase()
          .includes(busca.toLowerCase());

      return bateCategoria && bateBusca;
    });
  }, [categoriaSelecionada, busca]);

  const categorias = [
    "Todos",
    "Burgers",
    "Pizzas",
    "Sushi",
    "Bebidas",
  ];

  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <div>
          <h1>Restaurant System</h1>

          <div className={styles.headerActions}>
  <button
    className={`${styles.headerButton} ${
      cardapio ? styles.activeHeaderButton : ""
    }`}
    onClick={() => {
      setCardapio(true);
      setReservaMesa(false);
      setHabilitarCarrinho(true);
      setMesaSelecionada(null);
    }}
  >
    🍔 Cardápio
  </button>

  <button
    className={`${styles.headerButton} ${
      reservaMesa ? styles.activeHeaderButton : ""
    }`}
    onClick={() => {
      setCardapio(false);
      setReservaMesa(true);
      setHabilitarCarrinho(false);
    }}
  >
    🍽️ Reservar Mesa
  </button>
</div>

        </div>


        {habilitarCarrinho && (
          <button
            className={styles.cartButton}
            onClick={() =>
              setAbrirCarrinho(true)
            }
          >
            <ShoppingCart size={20} />

            <span>{itensCarrinho.length}</span>
          </button>
        )}

      </header>


      {abrirCarrinho && (
  <div className={styles.cartModal}>
    <div className={styles.cartContent}>
      <div className={styles.cartHeader}>
        <h2>Seu Carrinho</h2>

        <button
          onClick={() =>
            setAbrirCarrinho(false)
          }
        >
          ✕
        </button>
      </div>

      {itensCarrinho.length === 0 ? (
        <p className={styles.emptyCart}>
          Seu carrinho está vazio
        </p>
      ) : (
        <>
          <div className={styles.cartItems}>
            {itensCarrinho.map((item, index) => (
              <div
                key={index}
                className={styles.cartItem}
              >
                <img
                  src={item.imagem}
                  alt={item.nome}
                />

                <div>
                  <h4>{item.nome}</h4>

                  <p>
                    {item.preco.toLocaleString(
                      "pt-BR",
                      {
                        style: "currency",
                        currency: "BRL",
                      }
                    )}
                  </p>
                </div>
              </div>
            ))}
          </div>

          <div className={styles.cartFooter}>
            <strong>
              Total:{" "}
              {itensCarrinho
                .reduce(
                  (acc, item) =>
                    acc + item.preco,
                  0
                )
                .toLocaleString("pt-BR", {
                  style: "currency",
                  currency: "BRL",
                })}
            </strong>

            <button>
              Finalizar Pedido
            </button>
          </div>
        </>
      )}
    </div>
  </div>
)}

      {cardapio && (
        <div>



          <section className={styles.hero}>
            <div className={styles.overlay} />

            <div className={styles.heroContent}>
              <span>Delivery rápido 🚀</span>

              <h2>
                Sabores incríveis
                <br />
                direto na sua casa
              </h2>

              <p>
                Faça pedidos online de forma simples,
                rápida e moderna.
              </p>
            </div>
          </section>

          <section className={styles.searchArea}>
            <div className={styles.searchBox}>
              <Search size={18} />

              <input
                type="text"
                placeholder="Buscar pratos..."
                value={busca}
                onChange={(e) =>
                  setBusca(e.target.value)
                }
              />
            </div>
          </section>

          <section className={styles.categories}>
            {categorias.map((categoria) => (
              <button
                key={categoria}
                className={
                  categoriaSelecionada === categoria
                    ? styles.active
                    : ""
                }
                onClick={() =>
                  setCategoriaSelecionada(categoria)
                }
              >
                {categoria}
              </button>
            ))}
          </section>

          <section className={styles.products}>
            {produtosFiltrados.map((produto) => (
              <div key={produto.id} className={styles.card}>
                <img
                  src={produto.imagem}
                  alt={produto.nome}
                />

                <div className={styles.cardContent}>
                  <div className={styles.rating}>
                    <Star size={14} fill="currentColor" />

                    <span>4.9</span>
                  </div>

                  <h3>{produto.nome}</h3>

                  <p>{produto.descricao}</p>

                  <div className={styles.footerCard}>
                    <div>
                      <strong>
                        {produto.preco.toLocaleString(
                          "pt-BR",
                          {
                            style: "currency",
                            currency: "BRL",
                          }
                        )}
                      </strong>

                      <span>
                        <Clock3 size={14} />
                        25 min
                      </span>
                    </div>

                    <button
                      onClick={() =>
                        adicionarCarrinho(produto.id)
                      }
                    >
                      <Plus size={18} />
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </section>
        </div>
      )}

      {reservaMesa && (
        <section className={styles.mesasSection}>
  <h2>Mesas Disponíveis</h2>

  <div className={styles.mesasGrid}>
    {mesas.map((mesa) => (
      <div
        key={mesa.id}
        className={`${styles.mesaCard}
          ${
            mesaSelecionada?.id === mesa.id
              ? styles.mesaSelecionada
              : ""
          }`}
      >
        <h3>Mesa {mesa.numero}</h3>

        <p>
          Capacidade:
          {mesa.capacidade} pessoas
        </p>

        <span>
          {mesa.status}
        </span>

        <button
          onClick={() =>
            setMesaSelecionada(mesa)
          }
        >
          Selecionar
        </button>
      </div>
    ))}
  </div>
</section>
      )}
      
      {mesaSelecionada && (
  <section
    className={styles.reservaContainer}
  >
    <div
      className={styles.reservaCard}
    >
      <h2>
        Reserva da Mesa 
        {" "}
        {mesaSelecionada.numero}
      </h2>

      <p>
        Capacidade:
        {" "}
        {mesaSelecionada.capacidade}
        {" "}
        pessoas
      </p>

      <div
        className={styles.formGrid}
      >
        <div
          className={styles.formGroup}
        >
          <label>Data</label>

          <input type="date" />
        </div>

        <div
          className={styles.formGroup}
        >
          <label>Horário</label>

          <input type="time" />
        </div>
      </div>

      <button
        className={
          styles.reserveButton
        }
      >
        Confirmar Reserva
      </button>
    </div>
  </section>
)}

    </div>
  );
}