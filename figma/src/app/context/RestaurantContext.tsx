import { createContext, useContext, useState, ReactNode } from 'react';

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
  clientName: string;
  clientPhone: string;
  tableId: string;
  date: string;
  time: string;
  guests: number;
  status: 'pending' | 'confirmed' | 'completed' | 'cancelled';
}

export interface Order {
  id: string;
  type: 'online' | 'in-person';
  clientName: string;
  items: { menuItemId: string; quantity: number; name: string; price: number }[];
  total: number;
  status: 'pending' | 'preparing' | 'ready' | 'delivered' | 'completed';
  paymentMethod: string;
  tableId?: string;
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
  type: 'in' | 'out';
  quantity: number;
  date: string;
  reason: string;
}

interface RestaurantContextType {
  menuItems: MenuItem[];
  tables: Table[];
  reservations: Reservation[];
  orders: Order[];
  ingredients: Ingredient[];
  stockMovements: StockMovement[];
  addMenuItem: (item: Omit<MenuItem, 'id'>) => void;
  updateMenuItem: (id: string, item: Partial<MenuItem>) => void;
  deleteMenuItem: (id: string) => void;
  addTable: (table: Omit<Table, 'id'>) => void;
  updateTable: (id: string, table: Partial<Table>) => void;
  addReservation: (reservation: Omit<Reservation, 'id'>) => void;
  updateReservation: (id: string, reservation: Partial<Reservation>) => void;
  addOrder: (order: Omit<Order, 'id' | 'createdAt'>) => void;
  updateOrder: (id: string, order: Partial<Order>) => void;
  addIngredient: (ingredient: Omit<Ingredient, 'id'>) => void;
  updateIngredient: (id: string, ingredient: Partial<Ingredient>) => void;
  addStockMovement: (movement: Omit<StockMovement, 'id'>) => void;
}

const RestaurantContext = createContext<RestaurantContextType | undefined>(undefined);

const initialMenuItems: MenuItem[] = [
  { id: '1', name: 'Pizza Margherita', category: 'Pizza', price: 35.90, description: 'Molho de tomate, mussarela e manjericão', image: 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=400', available: true },
  { id: '2', name: 'Hambúrguer Artesanal', category: 'Hambúrguer', price: 28.50, description: 'Blend da casa, queijo cheddar, bacon', image: 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400', available: true },
  { id: '3', name: 'Salada Caesar', category: 'Salada', price: 22.00, description: 'Alface, frango grelhado, croutons, parmesão', image: 'https://images.unsplash.com/photo-1546793665-c74683f339c1?w=400', available: true },
  { id: '4', name: 'Suco Natural', category: 'Bebida', price: 8.00, description: 'Laranja, limão ou morango', image: 'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=400', available: true },
];

const initialTables: Table[] = [
  { id: '1', number: 1, capacity: 2, status: 'available' },
  { id: '2', number: 2, capacity: 4, status: 'available' },
  { id: '3', number: 3, capacity: 4, status: 'occupied' },
  { id: '4', number: 4, capacity: 6, status: 'available' },
  { id: '5', number: 5, capacity: 2, status: 'reserved' },
  { id: '6', number: 6, capacity: 8, status: 'available' },
];

const initialIngredients: Ingredient[] = [
  { id: '1', name: 'Farinha de Trigo', quantity: 50, unit: 'kg', minStock: 20 },
  { id: '2', name: 'Tomate', quantity: 30, unit: 'kg', minStock: 15 },
  { id: '3', name: 'Mussarela', quantity: 25, unit: 'kg', minStock: 10 },
  { id: '4', name: 'Alface', quantity: 15, unit: 'kg', minStock: 5 },
  { id: '5', name: 'Carne Bovina', quantity: 40, unit: 'kg', minStock: 20 },
];

export function RestaurantProvider({ children }: { children: ReactNode }) {
  const [menuItems, setMenuItems] = useState<MenuItem[]>(initialMenuItems);
  const [tables, setTables] = useState<Table[]>(initialTables);
  const [reservations, setReservations] = useState<Reservation[]>([]);
  const [orders, setOrders] = useState<Order[]>([]);
  const [ingredients, setIngredients] = useState<Ingredient[]>(initialIngredients);
  const [stockMovements, setStockMovements] = useState<StockMovement[]>([]);

  const addMenuItem = (item: Omit<MenuItem, 'id'>) => {
    setMenuItems([...menuItems, { ...item, id: Date.now().toString() }]);
  };

  const updateMenuItem = (id: string, item: Partial<MenuItem>) => {
    setMenuItems(menuItems.map(mi => mi.id === id ? { ...mi, ...item } : mi));
  };

  const deleteMenuItem = (id: string) => {
    setMenuItems(menuItems.filter(mi => mi.id !== id));
  };

  const addTable = (table: Omit<Table, 'id'>) => {
    setTables([...tables, { ...table, id: Date.now().toString() }]);
  };

  const updateTable = (id: string, table: Partial<Table>) => {
    setTables(tables.map(t => t.id === id ? { ...t, ...table } : t));
  };

  const addReservation = (reservation: Omit<Reservation, 'id'>) => {
    setReservations([...reservations, { ...reservation, id: Date.now().toString() }]);
  };

  const updateReservation = (id: string, reservation: Partial<Reservation>) => {
    setReservations(reservations.map(r => r.id === id ? { ...r, ...reservation } : r));
  };

  const addOrder = (order: Omit<Order, 'id' | 'createdAt'>) => {
    setOrders([...orders, { ...order, id: Date.now().toString(), createdAt: new Date().toISOString() }]);
  };

  const updateOrder = (id: string, order: Partial<Order>) => {
    setOrders(orders.map(o => o.id === id ? { ...o, ...order } : o));
  };

  const addIngredient = (ingredient: Omit<Ingredient, 'id'>) => {
    setIngredients([...ingredients, { ...ingredient, id: Date.now().toString() }]);
  };

  const updateIngredient = (id: string, ingredient: Partial<Ingredient>) => {
    setIngredients(ingredients.map(i => i.id === id ? { ...i, ...ingredient } : i));
  };

  const addStockMovement = (movement: Omit<StockMovement, 'id'>) => {
    const newMovement = { ...movement, id: Date.now().toString() };
    setStockMovements([...stockMovements, newMovement]);

    const ingredient = ingredients.find(i => i.id === movement.ingredientId);
    if (ingredient) {
      const newQuantity = movement.type === 'in'
        ? ingredient.quantity + movement.quantity
        : ingredient.quantity - movement.quantity;
      updateIngredient(movement.ingredientId, { quantity: Math.max(0, newQuantity) });
    }
  };

  return (
    <RestaurantContext.Provider value={{
      menuItems, tables, reservations, orders, ingredients, stockMovements,
      addMenuItem, updateMenuItem, deleteMenuItem,
      addTable, updateTable,
      addReservation, updateReservation,
      addOrder, updateOrder,
      addIngredient, updateIngredient,
      addStockMovement,
    }}>
      {children}
    </RestaurantContext.Provider>
  );
}

export function useRestaurant() {
  const context = useContext(RestaurantContext);
  if (!context) {
    throw new Error('useRestaurant must be used within RestaurantProvider');
  }
  return context;
}
