import { MenuItem, Table, Reservation, Order, Ingredient, StockMovement, User } from '../types';

const API_BASE_URL = 'https://system-for-order-management-production.up.railway.app';

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const url = `${API_BASE_URL}${path}`;
  const headers = {
    'Content-Type': 'application/json',
    ...(options?.headers || {}),
  };

  const response = await fetch(url, {
    ...options,
    headers,
  });

  if (!response.ok) {
    let errorMessage = `Erro na requisição: ${response.status} ${response.statusText}`;
    try {
      const text = await response.text();
      if (text) {
        errorMessage = text;
      }
    } catch (_) {}
    throw new Error(errorMessage);
  }

  // If response is 204 No Content or body is empty
  const contentType = response.headers.get('content-type');
  if (response.status === 204 || !contentType || !contentType.includes('application/json')) {
    return {} as T;
  }

  return response.json();
}

export const api = {
  // Auth
  async login(email: string, senha: string): Promise<User> {
    interface BackendUser {
      id: number;
      nome: string;
      email: string;
      role: 'admin' | 'client' | 'waiter';
    }
    const data = await request<BackendUser>('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, senha }),
    });
    return {
      id: data.id.toString(),
      name: data.nome,
      email: data.email,
      role: data.role,
    };
  },

  async registerClient(clientData: any): Promise<any> {
    return request<any>('/clientes', {
      method: 'POST',
      body: JSON.stringify(clientData),
    });
  },

  // Menu Items (Cardapio)
  async getMenuItems(): Promise<MenuItem[]> {
    interface BackendItem {
      id: number;
      nome: string;
      descricao: string;
      preco: number;
      categoriaItem: 'COMIDA' | 'BEBIDA' | 'SOBREMESA';
      urlFoto: string;
    }
    const data = await request<BackendItem[]>('/itens');
    return data.map((item) => ({
      id: item.id.toString(),
      name: item.nome,
      category: item.categoriaItem === 'COMIDA' ? 'Comida' : item.categoriaItem === 'BEBIDA' ? 'Bebida' : 'Sobremesa',
      price: item.preco,
      description: item.descricao,
      image: item.urlFoto || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400',
      available: true,
    }));
  },

  async createMenuItem(item: Omit<MenuItem, 'id'>): Promise<any> {
    const categoryMap: Record<string, string> = {
      'Comida': 'COMIDA',
      'Bebida': 'BEBIDA',
      'Sobremesa': 'SOBREMESA',
    };
    const backendCategory = categoryMap[item.category] || 'COMIDA';
    return request<any>('/itens', {
      method: 'POST',
      body: JSON.stringify({
        nome: item.name,
        preco: item.price,
        descricao: item.description,
        categoria: backendCategory,
        urlFoto: item.image,
      }),
    });
  },

  async updateMenuItem(id: string, item: Partial<MenuItem>): Promise<any> {
    const categoryMap: Record<string, string> = {
      'Comida': 'COMIDA',
      'Bebida': 'BEBIDA',
      'Sobremesa': 'SOBREMESA',
    };
    const payload: any = {};
    if (item.name !== undefined) payload.nome = item.name;
    if (item.price !== undefined) payload.preco = item.price;
    if (item.description !== undefined) payload.descricao = item.description;
    if (item.category !== undefined) payload.categoria = categoryMap[item.category] || 'COMIDA';
    if (item.image !== undefined) payload.urlFoto = item.image;

    return request<any>(`/itens/${id}`, {
      method: 'PUT',
      body: JSON.stringify(payload),
    });
  },

  async deleteMenuItem(id: string): Promise<any> {
    return request<any>(`/itens/${id}`, {
      method: 'DELETE',
    });
  },

  // Tables (Mesas)
  async getTables(): Promise<Table[]> {
    interface BackendTable {
      id: number;
      numero: number;
      capacidade: number;
      status: 'LIVRE' | 'OCUPADA' | 'RESERVADA';
    }
    const data = await request<BackendTable[]>('/mesas');
    return data.map((table) => ({
      id: table.id.toString(),
      number: table.numero,
      capacity: table.capacidade,
      status: table.status === 'LIVRE' ? 'available' : table.status === 'OCUPADA' ? 'occupied' : 'reserved',
    }));
  },

  async createTable(table: Omit<Table, 'id'>): Promise<any> {
    const statusMap: Record<string, string> = {
      'available': 'LIVRE',
      'occupied': 'OCUPADA',
      'reserved': 'RESERVADA',
    };
    return request<any>('/mesas', {
      method: 'POST',
      body: JSON.stringify({
        numero: table.number,
        capacidade: table.capacity,
        status: statusMap[table.status] || 'LIVRE',
      }),
    });
  },

  async updateTableStatus(id: string, status: 'available' | 'occupied' | 'reserved'): Promise<any> {
    const statusMap: Record<string, string> = {
      'available': 'LIVRE',
      'occupied': 'OCUPADA',
      'reserved': 'RESERVADA',
    };
    return request<any>(`/mesas/${id}`, {
      method: 'PUT',
      body: JSON.stringify(statusMap[status]),
    });
  },

  // Reservations (Reservas)
  async getReservations(): Promise<Reservation[]> {
    interface BackendReservation {
      id: number;
      numeroMesa: number;
      dataHora: string; // dd/MM/yyyy HH:mm or ISO
      status: 'CONFIRMADA' | 'CANCELADA' | 'FINALIZADA';
      valor: number;
      clienteId?: number;
      clienteNome?: string;
    }
    const data = await request<BackendReservation[]>('/reservas');
    return data.map((res) => {
      // Parse dataHora: "09/06/2026 18:30"
      let date = '';
      let time = '';
      if (res.dataHora) {
        const parts = res.dataHora.split(' ');
        if (parts[0]) date = parts[0];
        if (parts[1]) time = parts[1];
      }
      return {
        id: res.id.toString(),
        clientId: res.clienteId?.toString(),
        clientName: res.clienteNome || 'Cliente da Reserva',
        clientPhone: '',
        tableId: '', // We don't have tableId in list, but we have tableNumber
        tableNumber: res.numeroMesa,
        date: date,
        time: time,
        guests: 4, // placeholder
        status: res.status === 'CONFIRMADA' ? 'confirmed' : res.status === 'FINALIZADA' ? 'completed' : 'cancelled',
        value: res.valor,
      };
    });
  },

  async createReservation(res: Omit<Reservation, 'id'>): Promise<any> {
    // Format date and time to dd/MM/yyyy HH:mm
    let formattedDate = res.date; // expects yyyy-MM-dd
    if (res.date.includes('-')) {
      const dateParts = res.date.split('-'); // [yyyy, MM, dd]
      formattedDate = `${dateParts[2]}/${dateParts[1]}/${dateParts[0]}`;
    }
    const formattedDataHora = `${formattedDate} ${res.time}`;

    return request<any>('/reservas', {
      method: 'POST',
      body: JSON.stringify({
        mesa: { id: parseInt(res.tableId || '1') },
        cliente: { id: parseInt(localStorage.getItem('userId') || '1') },
        dataHora: formattedDataHora,
        status: 'CONFIRMADA',
        valorDaReserva: res.value || 50.00,
      }),
    });
  },

  async cancelReservation(id: string): Promise<any> {
    return request<any>(`/reservas/${id}/cancelar`, {
      method: 'DELETE',
    });
  },

  // Orders (Pedidos)
  async getOrders(): Promise<Order[]> {
    interface BackendOrderPresencial {
      id: number;
      valorFinal: number;
      status: 'PEDIDO_EM_PREPARO' | 'PEDIDO_PRONTO' | 'PEDIDO_ENTREGUE' | 'PEDIDO_CANCELADO';
      formaDePagamento: string;
      dataHora: string;
      numeroMesa: number;
      nomeGarcom: string;
    }

    interface BackendOrderOnline {
      id: number;
      valorTotal: number;
      status: 'PEDIDO_EM_PREPARO' | 'PEDIDO_PRONTO' | 'PEDIDO_ENTREGUE' | 'PEDIDO_CANCELADO';
      formaPagamento: string;
      dataHora: string;
      itens: string[];
      clienteId?: number;
      clienteNome?: string;
    }

    const presencias = await request<BackendOrderPresencial[]>('/pedidosPresenciais');
    const onlines = await request<BackendOrderOnline[]>('/pedidosOnline');

    const statusMap = (s: string): Order['status'] => {
      switch (s) {
        case 'PEDIDO_EM_PREPARO': return 'preparing';
        case 'PEDIDO_PRONTO': return 'ready';
        case 'PEDIDO_ENTREGUE': return 'delivered';
        case 'PEDIDO_CANCELADO': return 'cancelled';
        default: return 'pending';
      }
    };

    const paymentMap = (p: string): string => {
      switch (p) {
        case 'DINHEIRO': return 'Dinheiro';
        case 'PIX': return 'Pix';
        case 'CARTAO_CREDITO': return 'Cartão de Crédito';
        case 'CARTAO_DEBITO': return 'Cartão de Débito';
        default: return p;
      }
    };

    const ordersPresenciais: Order[] = presencias.map((o) => ({
      id: `presencial-${o.id}`,
      type: 'in-person',
      clientName: o.nomeGarcom ? `Garçom: ${o.nomeGarcom}` : `Mesa ${o.numeroMesa}`,
      items: [], // Presencial endpoint list does not return full details, we use fallback/empty list
      total: o.valorFinal,
      status: statusMap(o.status),
      paymentMethod: paymentMap(o.formaDePagamento),
      tableId: '',
      tableNumber: o.numeroMesa,
      createdAt: o.dataHora || new Date().toISOString(),
    }));

    const ordersOnlines: Order[] = onlines.map((o) => ({
      id: `online-${o.id}`,
      type: 'online',
      clientId: o.clienteId?.toString(),
      clientName: o.clienteNome || 'Cliente Online',
      items: (o.itens || []).map((name) => ({
        menuItemId: '',
        quantity: 1,
        name: name,
        price: 0,
      })),
      total: o.valorTotal,
      status: statusMap(o.status),
      paymentMethod: paymentMap(o.formaPagamento),
      createdAt: o.dataHora || new Date().toISOString(),
    }));

    return [...ordersPresenciais, ...ordersOnlines];
  },

  async createPresencialOrder(orderData: { tableId: string; waiterId: string; paymentMethod: string; items: { id: string }[] }): Promise<any> {
    const paymentMap: Record<string, string> = {
      'Dinheiro': 'DINHEIRO',
      'Pix': 'PIX',
      'Cartão de Crédito': 'CARTAO_CREDITO',
      'Cartão de Débito': 'CARTAO_DEBITO',
    };
    return request<any>('/pedidosPresenciais', {
      method: 'POST',
      body: JSON.stringify({
        mesa: { id: parseInt(orderData.tableId) },
        garcom: { id: parseInt(orderData.waiterId) },
        formaPagamento: paymentMap[orderData.paymentMethod] || 'PIX',
        itens: orderData.items.map((it) => ({ id: parseInt(it.id) })),
      }),
    });
  },

  async createOnlineOrder(orderData: { clientName: string; paymentMethod: string; items: { id: string }[] }): Promise<any> {
    const paymentMap: Record<string, string> = {
      'Dinheiro': 'DINHEIRO',
      'Pix': 'PIX',
      'Cartão de Crédito': 'CARTAO_CREDITO',
      'Cartão de Débito': 'CARTAO_DEBITO',
    };
    return request<any>('/pedidosOnline', {
      method: 'POST',
      body: JSON.stringify({
        clienteId: parseInt(localStorage.getItem('userId') || '1'),
        itensIds: orderData.items.map((it) => parseInt(it.id)),
        formaPagamento: paymentMap[orderData.paymentMethod] || 'PIX',
        status: 'PEDIDO_EM_PREPARO',
      }),
    });
  },

  async finalizeOrder(id: string, type: 'online' | 'in-person'): Promise<any> {
    const rawId = id.split('-')[1];
    const path = type === 'online' ? `/pedidosOnline/${rawId}/finalizar` : `/pedidosPresenciais/${rawId}/finalizar`;
    return request<any>(path, {
      method: 'PUT',
    });
  },

  async cancelOrder(id: string, type: 'online' | 'in-person'): Promise<any> {
    const rawId = id.split('-')[1];
    const path = type === 'online' ? `/pedidosOnline/${rawId}/cancelar` : `/pedidosPresenciais/${rawId}/cancelar`;
    return request<any>(path, {
      method: 'DELETE',
    });
  },

  // Ingredients (Ingredientes)
  async getIngredients(): Promise<Ingredient[]> {
    interface BackendIngredient {
      id: number;
      nome: string;
      quantidade: number;
    }
    const data = await request<BackendIngredient[]>('/ingredientes');
    return data.map((ing) => ({
      id: ing.id.toString(),
      name: ing.nome,
      quantity: ing.quantidade,
      unit: 'un', // default fallback unit
      minStock: 10, // default fallback min stock
    }));
  },

  async createIngredient(ing: Omit<Ingredient, 'id'>): Promise<any> {
    return request<any>('/ingredientes', {
      method: 'POST',
      body: JSON.stringify({
        nome: ing.name,
        quantidade: ing.quantity,
      }),
    });
  },

  async updateIngredient(id: string, ing: Partial<Ingredient>): Promise<any> {
    return request<any>(`/ingredientes/${id}`, {
      method: 'PUT',
      body: JSON.stringify({
        nome: ing.name,
        quantidade: ing.quantity,
      }),
    });
  },

  // Stock movements (Movimentacoes)
  async getStockMovements(): Promise<StockMovement[]> {
    interface BackendMovement {
      id: number;
      ingrediente: { id: number; nome: string };
      quantidade: number;
      tipo: 'ENTRADA' | 'SAIDA';
      dataHora: string;
    }
    const data = await request<BackendMovement[]>('/movimentacoes');
    return data.map((mov) => ({
      id: mov.id.toString(),
      ingredientId: mov.ingrediente?.id?.toString() || '',
      ingredientName: mov.ingrediente?.nome || '',
      type: mov.tipo === 'ENTRADA' ? 'in' : 'out',
      quantity: mov.quantidade,
      date: mov.dataHora || new Date().toISOString(),
      reason: mov.tipo === 'ENTRADA' ? 'Entrada manual' : 'Saída manual',
    }));
  },

  async addStockMovement(mov: { ingredientId: string; type: 'in' | 'out'; quantity: number }): Promise<any> {
    const path = mov.type === 'in' ? '/movimentacoes/entrada' : '/movimentacoes/saida';
    return request<any>(path, {
      method: 'POST',
      body: JSON.stringify({
        idIngrediente: parseInt(mov.ingredientId),
        quantidade: mov.quantity,
      }),
    });
  },
};
