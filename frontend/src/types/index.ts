export type UserRole = 'admin' | 'client' | 'waiter';

export interface User {
  id: string;
  name: string;
  email: string;
  role: UserRole;
}

export interface MenuItem {
  id: string;
  name: string;
  category: string;
  price: number;
  description: string;
  image: string;
  available: boolean;
}

export interface Table {
  id: string;
  number: number;
  capacity: number;
  status: 'available' | 'occupied' | 'reserved';
}

export interface Reservation {
  id: string;
  clientId?: string;
  clientName: string;
  clientPhone: string;
  tableId: string;
  tableNumber?: number;
  date: string;
  time: string;
  guests: number;
  status: 'pending' | 'confirmed' | 'completed' | 'cancelled';
  value?: number;
}

export interface Order {
  id: string;
  type: 'online' | 'in-person';
  clientId?: string;
  clientName: string;
  items: { menuItemId: string; quantity: number; name: string; price: number }[];
  total: number;
  status: 'pending' | 'preparing' | 'ready' | 'delivered' | 'completed' | 'cancelled';
  paymentMethod: string;
  tableId?: string;
  tableNumber?: number;
  deliveryAddress?: string;
  createdAt: string;
}

export interface Ingredient {
  id: string;
  name: string;
  quantity: number;
  unit: string;
  minStock: number;
}

export interface StockMovement {
  id: string;
  ingredientId: string;
  ingredientName?: string;
  type: 'in' | 'out';
  quantity: number;
  date: string;
  reason: string;
}
