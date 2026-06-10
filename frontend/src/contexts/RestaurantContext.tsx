"use client"
import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { api } from '../services/api';
import { MenuItem, Table, Reservation, Order, Ingredient, StockMovement } from '../types';

interface RestaurantContextType {
  menuItems: MenuItem[];
  tables: Table[];
  reservations: Reservation[];
  orders: Order[];
  ingredients: Ingredient[];
  stockMovements: StockMovement[];
  refreshData: () => Promise<void>;
  addMenuItem: (item: Omit<MenuItem, 'id'>) => Promise<void>;
  updateMenuItem: (id: string, item: Partial<MenuItem>) => Promise<void>;
  deleteMenuItem: (id: string) => Promise<void>;
  addTable: (table: Omit<Table, 'id'>) => Promise<void>;
  updateTable: (id: string, table: Partial<Table>) => Promise<void>;
  addReservation: (reservation: Omit<Reservation, 'id'>) => Promise<void>;
  updateReservation: (id: string, reservation: Partial<Reservation>) => Promise<void>;
  addOrder: (order: Omit<Order, 'id' | 'createdAt'>) => Promise<void>;
  updateOrder: (id: string, order: Partial<Order>) => Promise<void>;
  addIngredient: (ingredient: Omit<Ingredient, 'id'>) => Promise<void>;
  updateIngredient: (id: string, ingredient: Partial<Ingredient>) => Promise<void>;
  addStockMovement: (movement: Omit<StockMovement, 'id' | 'ingredientName'>) => Promise<void>;
}

const RestaurantContext = createContext<RestaurantContextType | undefined>(undefined);

export function RestaurantProvider({ children }: { children: ReactNode }) {
  const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
  const [tables, setTables] = useState<Table[]>([]);
  const [reservations, setReservations] = useState<Reservation[]>([]);
  const [orders, setOrders] = useState<Order[]>([]);
  const [ingredients, setIngredients] = useState<Ingredient[]>([]);
  const [stockMovements, setStockMovements] = useState<StockMovement[]>([]);

  const refreshData = async () => {
    try {
      const [items, tbls, ress, ords, ings, movs] = await Promise.all([
        api.getMenuItems(),
        api.getTables(),
        api.getReservations(),
        api.getOrders(),
        api.getIngredients(),
        api.getStockMovements()
      ]);
      setMenuItems(items);
      setTables(tbls);
      setReservations(ress);
      setOrders(ords);
      setIngredients(ings);
      setStockMovements(movs);
    } catch (error) {
      console.error('Error refreshing data from backend:', error);
    }
  };

  useEffect(() => {
    refreshData();
  }, []);

  const addMenuItem = async (item: Omit<MenuItem, 'id'>) => {
    await api.createMenuItem(item);
    await refreshData();
  };

  const updateMenuItem = async (id: string, item: Partial<MenuItem>) => {
    await api.updateMenuItem(id, item);
    await refreshData();
  };

  const deleteMenuItem = async (id: string) => {
    await api.deleteMenuItem(id);
    await refreshData();
  };

  const addTable = async (table: Omit<Table, 'id'>) => {
    await api.createTable(table);
    await refreshData();
  };

  const updateTable = async (id: string, table: Partial<Table>) => {
    if (table.status) {
      await api.updateTableStatus(id, table.status);
    }
    await refreshData();
  };

  const addReservation = async (reservation: Omit<Reservation, 'id'>) => {
    await api.createReservation(reservation);
    await refreshData();
  };

  const updateReservation = async (id: string, reservation: Partial<Reservation>) => {
    if (reservation.status === 'cancelled') {
      await api.cancelReservation(id);
    }
    await refreshData();
  };

  const addOrder = async (order: Omit<Order, 'id' | 'createdAt'>) => {
    if (order.type === 'online') {
      await api.createOnlineOrder({
        clientName: order.clientName,
        paymentMethod: order.paymentMethod,
        items: order.items.map((it) => ({ id: it.menuItemId })),
      });
    } else {
      await api.createPresencialOrder({
        tableId: order.tableId || '1',
        waiterId: localStorage.getItem('userId') || '1',
        paymentMethod: order.paymentMethod,
        items: order.items.map((it) => ({ id: it.menuItemId })),
      });
    }
    await refreshData();
  };

  const updateOrder = async (id: string, order: Partial<Order>) => {
    const isOnline = id.startsWith('online-');
    const type = isOnline ? 'online' : 'in-person';

    if (order.status === 'cancelled') {
      await api.cancelOrder(id, type);
    } else if (order.status === 'ready' || order.status === 'completed' || order.status === 'delivered') {
      if (isOnline) {
        // For online order status flow: pending -> preparing -> ready (iniciar) -> delivered (finalizar)
        if (order.status === 'ready') {
          // call iniciarEntrega
          const rawId = id.split('-')[1];
          await fetch(`http://localhost:8080/entregas/iniciar/${rawId}`, { method: 'PUT' });
        } else if (order.status === 'delivered' || order.status === 'completed') {
          // call finalizarEntrega
          const rawId = id.split('-')[1];
          await fetch(`http://localhost:8080/entregas/finalizar/${rawId}`, { method: 'PUT' });
        }
      } else {
        await api.finalizeOrder(id, type);
      }
    }
    await refreshData();
  };

  const addIngredient = async (ingredient: Omit<Ingredient, 'id'>) => {
    await api.createIngredient(ingredient);
    await refreshData();
  };

  const updateIngredient = async (id: string, ingredient: Partial<Ingredient>) => {
    await api.updateIngredient(id, ingredient);
    await refreshData();
  };

  const addStockMovement = async (movement: Omit<StockMovement, 'id' | 'ingredientName'>) => {
    await api.addStockMovement({
      ingredientId: movement.ingredientId,
      type: movement.type,
      quantity: movement.quantity,
    });
    await refreshData();
  };

  return (
    <RestaurantContext.Provider value={{
      menuItems, tables, reservations, orders, ingredients, stockMovements,
      refreshData,
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
