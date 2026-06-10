import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import { useAuth } from '../../context/AuthContext';
import { useRestaurant } from '../../context/RestaurantContext';
import {
  Users, UtensilsCrossed, LogOut, Plus, Minus, Check, X,
  Clock, DollarSign, ShoppingCart, AlertCircle
} from 'lucide-react';

type Tab = 'tables' | 'new-order' | 'orders';

export function WaiterDashboard() {
  const [activeTab, setActiveTab] = useState<Tab>('tables');
  const [selectedTable, setSelectedTable] = useState<string | null>(null);
  const [orderItems, setOrderItems] = useState<{ menuItemId: string; quantity: number }[]>([]);
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const { menuItems, tables, orders, updateTable, addOrder, updateOrder } = useRestaurant();

  useEffect(() => {
    if (!user || user.role !== 'waiter') {
      navigate('/');
    }
  }, [user, navigate]);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const addItemToOrder = (menuItemId: string) => {
    const existing = orderItems.find(item => item.menuItemId === menuItemId);
    if (existing) {
      setOrderItems(orderItems.map(item =>
        item.menuItemId === menuItemId
          ? { ...item, quantity: item.quantity + 1 }
          : item
      ));
    } else {
      setOrderItems([...orderItems, { menuItemId, quantity: 1 }]);
    }
  };

  const updateOrderItemQuantity = (menuItemId: string, delta: number) => {
    setOrderItems(orderItems.map(item =>
      item.menuItemId === menuItemId
        ? { ...item, quantity: Math.max(0, item.quantity + delta) }
        : item
    ).filter(item => item.quantity > 0));
  };

  const getOrderTotal = () => {
    return orderItems.reduce((total, item) => {
      const menuItem = menuItems.find(mi => mi.id === item.menuItemId);
      return total + (menuItem?.price || 0) * item.quantity;
    }, 0);
  };

  const createOrder = (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedTable || orderItems.length === 0) return;

    const formData = new FormData(e.currentTarget as HTMLFormElement);

    const items = orderItems.map(item => {
      const menuItem = menuItems.find(mi => mi.id === item.menuItemId)!;
      return {
        menuItemId: item.menuItemId,
        quantity: item.quantity,
        name: menuItem.name,
        price: menuItem.price,
      };
    });

    addOrder({
      type: 'in-person',
      clientName: formData.get('clientName') as string || 'Cliente',
      items,
      total: getOrderTotal(),
      status: 'pending',
      paymentMethod: formData.get('paymentMethod') as string,
      tableId: selectedTable,
    });

    updateTable(selectedTable, { status: 'occupied' });
    setOrderItems([]);
    setSelectedTable(null);
    setActiveTab('orders');
  };

  const inPersonOrders = orders.filter(o => o.type === 'in-person');

  return (
    <div className="min-h-screen bg-gradient-to-br from-green-50 via-white to-emerald-50">
      <header className="bg-white border-b border-gray-200 sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="bg-green-500 p-2 rounded-lg">
                <UtensilsCrossed className="w-6 h-6 text-white" />
              </div>
              <div>
                <h1 className="text-xl font-bold text-gray-900">Painel do Garçom</h1>
                <p className="text-sm text-gray-600">Bem-vindo, {user?.name}</p>
              </div>
            </div>
            <button
              onClick={handleLogout}
              className="flex items-center gap-2 px-4 py-2 text-gray-700 hover:bg-gray-100 rounded-lg transition"
            >
              <LogOut className="w-4 h-4" />
              Sair
            </button>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-6 py-6">
        <div className="flex gap-4 mb-6 overflow-x-auto pb-2">
          {[
            { id: 'tables', label: 'Mesas', icon: Users },
            { id: 'new-order', label: 'Novo Pedido', icon: Plus },
            { id: 'orders', label: 'Pedidos Ativos', icon: ShoppingCart },
          ].map((tab) => {
            const Icon = tab.icon;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id as Tab)}
                className={`flex items-center gap-2 px-4 py-2 rounded-lg font-medium transition whitespace-nowrap ${
                  activeTab === tab.id
                    ? 'bg-green-500 text-white shadow-lg'
                    : 'bg-white text-gray-700 hover:bg-gray-100'
                }`}
              >
                <Icon className="w-4 h-4" />
                {tab.label}
              </button>
            );
          })}
        </div>

        {activeTab === 'tables' && (
          <div className="space-y-6">
            <div className="flex items-center justify-between">
              <h2 className="text-2xl font-bold text-gray-900">Status das Mesas</h2>
              <div className="flex gap-4 text-sm">
                <div className="flex items-center gap-2">
                  <div className="w-3 h-3 bg-green-500 rounded-full"></div>
                  <span className="text-gray-600">Disponível</span>
                </div>
                <div className="flex items-center gap-2">
                  <div className="w-3 h-3 bg-red-500 rounded-full"></div>
                  <span className="text-gray-600">Ocupada</span>
                </div>
                <div className="flex items-center gap-2">
                  <div className="w-3 h-3 bg-yellow-500 rounded-full"></div>
                  <span className="text-gray-600">Reservada</span>
                </div>
              </div>
            </div>

            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
              {tables.map((table) => (
                <div
                  key={table.id}
                  className={`p-6 rounded-xl border-2 cursor-pointer transition hover:shadow-lg ${
                    table.status === 'available' ? 'bg-green-50 border-green-300' :
                    table.status === 'occupied' ? 'bg-red-50 border-red-300' :
                    'bg-yellow-50 border-yellow-300'
                  }`}
                  onClick={() => {
                    if (table.status === 'available') {
                      setSelectedTable(table.id);
                      setActiveTab('new-order');
                    }
                  }}
                >
                  <div className="text-center">
                    <Users className={`w-10 h-10 mx-auto mb-3 ${
                      table.status === 'available' ? 'text-green-600' :
                      table.status === 'occupied' ? 'text-red-600' :
                      'text-yellow-600'
                    }`} />
                    <div className="text-3xl font-bold text-gray-900 mb-1">
                      {table.number}
                    </div>
                    <div className="text-sm text-gray-600 mb-2">
                      {table.capacity} lugares
                    </div>
                    <select
                      value={table.status}
                      onChange={(e) => {
                        e.stopPropagation();
                        updateTable(table.id, { status: e.target.value as any });
                      }}
                      className={`w-full px-3 py-2 border-2 rounded-lg text-sm font-medium ${
                        table.status === 'available' ? 'border-green-300 bg-green-100 text-green-700' :
                        table.status === 'occupied' ? 'border-red-300 bg-red-100 text-red-700' :
                        'border-yellow-300 bg-yellow-100 text-yellow-700'
                      }`}
                      onClick={(e) => e.stopPropagation()}
                    >
                      <option value="available">Disponível</option>
                      <option value="occupied">Ocupada</option>
                      <option value="reserved">Reservada</option>
                    </select>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === 'new-order' && (
          <div className="space-y-6">
            <div className="flex items-center justify-between">
              <h2 className="text-2xl font-bold text-gray-900">Novo Pedido</h2>
              {selectedTable && (
                <div className="text-lg font-semibold text-green-600">
                  Mesa {tables.find(t => t.id === selectedTable)?.number}
                </div>
              )}
            </div>

            {!selectedTable ? (
              <div className="bg-white rounded-xl p-12 text-center border border-gray-200">
                <AlertCircle className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                <p className="text-gray-600 mb-4">Selecione uma mesa para criar um pedido</p>
                <button
                  onClick={() => setActiveTab('tables')}
                  className="bg-green-500 text-white px-6 py-3 rounded-lg hover:bg-green-600 transition"
                >
                  Ver Mesas
                </button>
              </div>
            ) : (
              <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                <div className="lg:col-span-2">
                  <div className="bg-white rounded-xl p-6 border border-gray-200 mb-6">
                    <h3 className="font-semibold text-gray-900 mb-4">Cardápio</h3>
                    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                      {menuItems.filter(item => item.available).map((item) => (
                        <div
                          key={item.id}
                          onClick={() => addItemToOrder(item.id)}
                          className="flex items-center gap-4 p-4 bg-gray-50 hover:bg-gray-100 rounded-lg cursor-pointer transition border border-gray-200"
                        >
                          <img src={item.image} alt={item.name} className="w-16 h-16 object-cover rounded-lg" />
                          <div className="flex-1">
                            <h4 className="font-medium text-gray-900">{item.name}</h4>
                            <p className="text-xs text-gray-600">{item.category}</p>
                            <p className="text-sm font-bold text-green-600 mt-1">
                              R$ {item.price.toFixed(2)}
                            </p>
                          </div>
                          <Plus className="w-5 h-5 text-green-600" />
                        </div>
                      ))}
                    </div>
                  </div>
                </div>

                <div className="lg:col-span-1">
                  <div className="bg-white rounded-xl p-6 border border-gray-200 sticky top-24">
                    <h3 className="font-semibold text-gray-900 mb-4">Itens do Pedido</h3>

                    {orderItems.length === 0 ? (
                      <div className="text-center py-8 text-gray-500">
                        <ShoppingCart className="w-12 h-12 mx-auto mb-2 text-gray-300" />
                        <p className="text-sm">Nenhum item adicionado</p>
                      </div>
                    ) : (
                      <>
                        <div className="space-y-3 mb-6 max-h-96 overflow-y-auto">
                          {orderItems.map((item) => {
                            const menuItem = menuItems.find(mi => mi.id === item.menuItemId)!;
                            return (
                              <div key={item.menuItemId} className="flex items-center gap-3 p-3 bg-gray-50 rounded-lg">
                                <img src={menuItem.image} alt={menuItem.name} className="w-12 h-12 object-cover rounded" />
                                <div className="flex-1 min-w-0">
                                  <div className="font-medium text-gray-900 text-sm truncate">{menuItem.name}</div>
                                  <div className="text-xs text-green-600 font-semibold">
                                    R$ {(menuItem.price * item.quantity).toFixed(2)}
                                  </div>
                                </div>
                                <div className="flex items-center gap-2">
                                  <button
                                    onClick={() => updateOrderItemQuantity(item.menuItemId, -1)}
                                    className="p-1 bg-white hover:bg-gray-100 rounded transition"
                                  >
                                    <Minus className="w-3 h-3" />
                                  </button>
                                  <span className="font-bold text-sm w-6 text-center">{item.quantity}</span>
                                  <button
                                    onClick={() => updateOrderItemQuantity(item.menuItemId, 1)}
                                    className="p-1 bg-white hover:bg-gray-100 rounded transition"
                                  >
                                    <Plus className="w-3 h-3" />
                                  </button>
                                </div>
                              </div>
                            );
                          })}
                        </div>

                        <form onSubmit={createOrder} className="space-y-4">
                          <div className="bg-gray-50 p-4 rounded-lg">
                            <div className="flex justify-between text-2xl font-bold text-gray-900">
                              <span>Total:</span>
                              <span className="text-green-600">R$ {getOrderTotal().toFixed(2)}</span>
                            </div>
                          </div>

                          <input
                            name="clientName"
                            type="text"
                            placeholder="Nome do cliente (opcional)"
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg"
                          />

                          <select
                            name="paymentMethod"
                            className="w-full px-4 py-3 border border-gray-300 rounded-lg"
                            required
                          >
                            <option value="">Forma de Pagamento</option>
                            <option value="credit">Cartão de Crédito</option>
                            <option value="debit">Cartão de Débito</option>
                            <option value="pix">PIX</option>
                            <option value="cash">Dinheiro</option>
                          </select>

                          <button
                            type="submit"
                            className="w-full bg-green-500 text-white py-3 rounded-lg font-semibold hover:bg-green-600 transition flex items-center justify-center gap-2"
                          >
                            <Check className="w-5 h-5" />
                            Confirmar Pedido
                          </button>

                          <button
                            type="button"
                            onClick={() => {
                              setOrderItems([]);
                              setSelectedTable(null);
                            }}
                            className="w-full bg-gray-200 text-gray-700 py-3 rounded-lg font-semibold hover:bg-gray-300 transition"
                          >
                            Cancelar
                          </button>
                        </form>
                      </>
                    )}
                  </div>
                </div>
              </div>
            )}
          </div>
        )}

        {activeTab === 'orders' && (
          <div className="space-y-6">
            <h2 className="text-2xl font-bold text-gray-900">Pedidos Presenciais Ativos</h2>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {inPersonOrders.filter(o => o.status !== 'completed').map((order) => {
                const table = tables.find(t => t.id === order.tableId);
                return (
                  <div key={order.id} className="bg-white rounded-xl p-6 border border-gray-200">
                    <div className="flex items-start justify-between mb-4">
                      <div>
                        <div className="text-lg font-bold text-gray-900">
                          Mesa {table?.number}
                        </div>
                        <div className="text-sm text-gray-600">{order.clientName}</div>
                        <div className="text-xs text-gray-500 flex items-center gap-1 mt-1">
                          <Clock className="w-3 h-3" />
                          {new Date(order.createdAt).toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })}
                        </div>
                      </div>
                      <div className="text-right">
                        <div className="text-2xl font-bold text-green-600">
                          R$ {order.total.toFixed(2)}
                        </div>
                      </div>
                    </div>

                    <div className="border-t border-gray-200 pt-3 mb-4">
                      {order.items.map((item, idx) => (
                        <div key={idx} className="flex justify-between text-sm py-1">
                          <span className="text-gray-700">{item.quantity}x {item.name}</span>
                          <span className="text-gray-900 font-medium">
                            R$ {(item.price * item.quantity).toFixed(2)}
                          </span>
                        </div>
                      ))}
                    </div>

                    <div className="space-y-2">
                      <select
                        value={order.status}
                        onChange={(e) => updateOrder(order.id, { status: e.target.value as any })}
                        className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm font-medium"
                      >
                        <option value="pending">Pendente</option>
                        <option value="preparing">Preparando</option>
                        <option value="ready">Pronto</option>
                        <option value="completed">Finalizado</option>
                      </select>

                      {order.status === 'completed' && (
                        <button
                          onClick={() => {
                            if (order.tableId) {
                              updateTable(order.tableId, { status: 'available' });
                            }
                          }}
                          className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600 transition text-sm"
                        >
                          Liberar Mesa
                        </button>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>

            {inPersonOrders.filter(o => o.status !== 'completed').length === 0 && (
              <div className="bg-white rounded-xl p-12 text-center border border-gray-200">
                <ShoppingCart className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                <p className="text-gray-600">Nenhum pedido ativo no momento</p>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
