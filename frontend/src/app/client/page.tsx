"use client"
import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '../../contexts/AuthContext';
import { useRestaurant } from '../../contexts/RestaurantContext';
import {
  ShoppingCart, Calendar, Clock, LogOut, UtensilsCrossed,
  Plus, Minus, Trash2, MapPin, CreditCard, Package
} from 'lucide-react';

type Tab = 'menu' | 'cart' | 'reservations' | 'orders';

export default function ClientPage() {
  const [activeTab, setActiveTab] = useState<Tab>('menu');
  const [cart, setCart] = useState<{ menuItemId: string; quantity: number }[]>([]);
  const [showReservationForm, setShowReservationForm] = useState(false);
  const [showCheckout, setShowCheckout] = useState(false);
  const { user, logout, loading } = useAuth();
  const router = useRouter();
  const { menuItems, tables, reservations, orders, addReservation, addOrder } = useRestaurant();

  useEffect(() => {
    if (!loading && (!user || user.role !== 'client')) {
      router.push('/');
    }
  }, [user, loading, router]);

  const handleLogout = () => {
    logout();
    router.push('/');
  };

  if (loading || !user || user.role !== 'client') {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-gray-600 font-medium animate-pulse">Carregando...</div>
      </div>
    );
  }

  const addToCart = (menuItemId: string) => {
    const existing = cart.find(item => item.menuItemId === menuItemId);
    if (existing) {
      setCart(cart.map(item =>
        item.menuItemId === menuItemId
          ? { ...item, quantity: item.quantity + 1 }
          : item
      ));
    } else {
      setCart([...cart, { menuItemId, quantity: 1 }]);
    }
  };

  const updateCartQuantity = (menuItemId: string, delta: number) => {
    setCart(cart.map(item =>
      item.menuItemId === menuItemId
        ? { ...item, quantity: Math.max(0, item.quantity + delta) }
        : item
    ).filter(item => item.quantity > 0));
  };

  const removeFromCart = (menuItemId: string) => {
    setCart(cart.filter(item => item.menuItemId !== menuItemId));
  };

  const getCartTotal = () => {
    return cart.reduce((total, item) => {
      const menuItem = menuItems.find(mi => mi.id === item.menuItemId);
      return total + (menuItem?.price || 0) * item.quantity;
    }, 0);
  };

  const handleCheckout = async (e: React.FormEvent) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget as HTMLFormElement);

    const orderItems = cart.map(item => {
      const menuItem = menuItems.find(mi => mi.id === item.menuItemId)!;
      return {
        menuItemId: item.menuItemId,
        quantity: item.quantity,
        name: menuItem.name,
        price: menuItem.price,
      };
    });

    await addOrder({
      type: 'online',
      clientName: user?.nome || '',
      items: orderItems,
      total: getCartTotal(),
      status: 'pending',
      paymentMethod: formData.get('paymentMethod') as string,
      deliveryAddress: formData.get('address') as string,
    });

    setCart([]);
    setShowCheckout(false);
    setActiveTab('orders');
  };

  // Filter reservations and orders for this user by id or name
  const userIdStr = user?.id?.toString();
  const userReservations = reservations.filter(r => r.clientId === userIdStr || r.clientName === user?.nome);
  const userOrders = orders.filter(o => o.clientId === userIdStr || o.clientName === user?.nome);

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50">
      <header className="bg-white border-b border-gray-200 sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="bg-blue-500 p-2 rounded-lg">
                <UtensilsCrossed className="w-6 h-6 text-white" />
              </div>
              <div>
                <h1 className="text-xl font-bold text-gray-900">Área do Cliente</h1>
                <p className="text-sm text-gray-600">Olá, {user?.nome}!</p>
              </div>
            </div>
            <div className="flex items-center gap-4">
              <button
                onClick={() => setActiveTab('cart')}
                className="relative p-2 hover:bg-gray-100 rounded-lg transition"
              >
                <ShoppingCart className="w-6 h-6 text-gray-700" />
                {cart.length > 0 && (
                  <span className="absolute -top-1 -right-1 bg-red-500 text-white text-xs w-5 h-5 rounded-full flex items-center justify-center">
                    {cart.reduce((sum, item) => sum + item.quantity, 0)}
                  </span>
                )}
              </button>
              <button
                onClick={handleLogout}
                className="flex items-center gap-2 px-4 py-2 text-gray-700 hover:bg-gray-100 rounded-lg transition"
              >
                <LogOut className="w-4 h-4" />
                Sair
              </button>
            </div>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-6 py-6">
        <div className="flex gap-4 mb-6 overflow-x-auto pb-2">
          {[
            { id: 'menu', label: 'Cardápio', icon: UtensilsCrossed },
            { id: 'cart', label: 'Carrinho', icon: ShoppingCart },
            { id: 'reservations', label: 'Minhas Reservas', icon: Calendar },
            { id: 'orders', label: 'Meus Pedidos', icon: Package },
          ].map((tab) => {
            const Icon = tab.icon;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id as Tab)}
                className={`flex items-center gap-2 px-4 py-2 rounded-lg font-medium transition whitespace-nowrap ${
                  activeTab === tab.id
                    ? 'bg-blue-500 text-white shadow-lg'
                    : 'bg-white text-gray-700 hover:bg-gray-100'
                }`}
              >
                <Icon className="w-4 h-4" />
                {tab.label}
                {tab.id === 'cart' && cart.length > 0 && (
                  <span className="bg-white text-blue-500 text-xs px-2 py-0.5 rounded-full font-bold">
                    {cart.reduce((sum, item) => sum + item.quantity, 0)}
                  </span>
                )}
              </button>
            );
          })}
        </div>

        {activeTab === 'menu' && (
          <div className="space-y-6">
            <div className="flex items-center justify-between">
              <h2 className="text-2xl font-bold text-gray-900">Cardápio</h2>
              <div className="text-sm text-gray-600">
                {menuItems.filter(item => item.available).length} itens disponíveis
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {menuItems.filter(item => item.available).map((item) => (
                <div key={item.id} className="bg-white rounded-xl shadow-lg overflow-hidden border border-gray-200 hover:shadow-xl transition flex flex-col justify-between">
                  <img src={item.image} alt={item.name} className="w-full h-48 object-cover" />
                  <div className="p-5 flex-1 flex flex-col justify-between">
                    <div>
                      <div className="flex items-start justify-between mb-2">
                        <div>
                          <h3 className="text-lg font-bold text-gray-900">{item.name}</h3>
                          <p className="text-sm text-gray-600">{item.category}</p>
                        </div>
                        <div className="text-xl font-bold text-green-600">
                          R$ {item.price.toFixed(2)}
                        </div>
                      </div>
                      <p className="text-sm text-gray-600 mb-4">{item.description}</p>
                    </div>
                    <button
                      onClick={() => addToCart(item.id)}
                      className="w-full bg-blue-500 text-white py-3 rounded-lg font-semibold hover:bg-blue-600 transition flex items-center justify-center gap-2"
                    >
                      <Plus className="w-4 h-4" />
                      Adicionar ao Carrinho
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === 'cart' && (
          <div className="space-y-6">
            <h2 className="text-2xl font-bold text-gray-900">Carrinho de Compras</h2>

            {cart.length === 0 ? (
              <div className="bg-white rounded-xl p-12 text-center border border-gray-200">
                <ShoppingCart className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                <p className="text-gray-600 mb-4">Seu carrinho está vazio</p>
                <button
                  onClick={() => setActiveTab('menu')}
                  className="bg-blue-500 text-white px-6 py-3 rounded-lg hover:bg-blue-600 transition"
                >
                  Ver Cardápio
                </button>
              </div>
            ) : (
              <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                <div className="lg:col-span-2 space-y-4">
                  {cart.map((item) => {
                    const menuItem = menuItems.find(mi => mi.id === item.menuItemId)!;
                    if (!menuItem) return null;
                    return (
                      <div key={item.menuItemId} className="bg-white rounded-xl p-5 border border-gray-200 flex items-center gap-4">
                        <img src={menuItem.image} alt={menuItem.name} className="w-24 h-24 object-cover rounded-lg" />
                        <div className="flex-1">
                          <h3 className="font-bold text-gray-900">{menuItem.name}</h3>
                          <p className="text-sm text-gray-600">{menuItem.category}</p>
                          <p className="text-lg font-bold text-green-600 mt-1">
                            R$ {menuItem.price.toFixed(2)}
                          </p>
                        </div>
                        <div className="flex items-center gap-3">
                          <button
                            onClick={() => updateCartQuantity(item.menuItemId, -1)}
                            className="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg transition"
                          >
                            <Minus className="w-4 h-4" />
                          </button>
                          <span className="font-bold text-lg w-8 text-center">{item.quantity}</span>
                          <button
                            onClick={() => updateCartQuantity(item.menuItemId, 1)}
                            className="p-2 bg-gray-100 hover:bg-gray-200 rounded-lg transition"
                          >
                            <Plus className="w-4 h-4" />
                          </button>
                        </div>
                        <div className="text-right">
                          <div className="text-xl font-bold text-gray-900">
                            R$ {(menuItem.price * item.quantity).toFixed(2)}
                          </div>
                          <button
                            onClick={() => removeFromCart(item.menuItemId)}
                            className="text-red-500 hover:text-red-700 mt-2"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </div>
                      </div>
                    );
                  })}
                </div>

                <div className="lg:col-span-1">
                  <div className="bg-white rounded-xl p-6 border border-gray-200 sticky top-24">
                    <h3 className="text-lg font-bold text-gray-900 mb-4">Resumo do Pedido</h3>
                    <div className="space-y-3 mb-6">
                      <div className="flex justify-between text-gray-600">
                        <span>Subtotal</span>
                        <span>R$ {getCartTotal().toFixed(2)}</span>
                      </div>
                      <div className="flex justify-between text-gray-600">
                        <span>Taxa de entrega</span>
                        <span>R$ 5.00</span>
                      </div>
                      <div className="border-t pt-3 flex justify-between text-lg font-bold text-gray-900">
                        <span>Total</span>
                        <span>R$ {(getCartTotal() + 5).toFixed(2)}</span>
                      </div>
                    </div>
                    <button
                      onClick={() => setShowCheckout(true)}
                      className="w-full bg-green-500 text-white py-3 rounded-lg font-semibold hover:bg-green-600 transition"
                    >
                      Finalizar Pedido
                    </button>
                  </div>
                </div>
              </div>
            )}

            {showCheckout && (
              <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-6 z-50">
                <div className="bg-white rounded-xl p-8 max-w-md w-full">
                  <h3 className="text-xl font-bold text-gray-900 mb-6">Finalizar Pedido</h3>
                  <form onSubmit={handleCheckout} className="space-y-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        <MapPin className="w-4 h-4 inline mr-1" />
                        Endereço de Entrega
                      </label>
                      <input
                        name="address"
                        type="text"
                        className="w-full px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-green-500"
                        placeholder="Rua, número, bairro"
                        required
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        <CreditCard className="w-4 h-4 inline mr-1" />
                        Forma de Pagamento
                      </label>
                      <select
                        name="paymentMethod"
                        className="w-full px-4 py-3 border border-gray-300 rounded-lg bg-white outline-none focus:ring-2 focus:ring-green-500"
                        required
                      >
                        <option value="">Selecione...</option>
                        <option value="credit">Cartão de Crédito</option>
                        <option value="debit">Cartão de Débito</option>
                        <option value="pix">PIX</option>
                        <option value="cash">Dinheiro</option>
                      </select>
                    </div>
                    <div className="bg-gray-50 p-4 rounded-lg">
                      <div className="flex justify-between text-sm mb-2">
                        <span>Subtotal:</span>
                        <span>R$ {getCartTotal().toFixed(2)}</span>
                      </div>
                      <div className="flex justify-between text-sm mb-2">
                        <span>Taxa de entrega:</span>
                        <span>R$ 5.00</span>
                      </div>
                      <div className="flex justify-between font-bold text-lg border-t pt-2">
                        <span>Total:</span>
                        <span>R$ {(getCartTotal() + 5).toFixed(2)}</span>
                      </div>
                    </div>
                    <div className="flex gap-3">
                      <button
                        type="button"
                        onClick={() => setShowCheckout(false)}
                        className="flex-1 px-6 py-3 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition"
                      >
                        Cancelar
                      </button>
                      <button
                        type="submit"
                        className="flex-1 px-6 py-3 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
                      >
                        Confirmar
                      </button>
                    </div>
                  </form>
                </div>
              </div>
            )}
          </div>
        )}

        {activeTab === 'reservations' && (
          <div className="space-y-6">
            <div className="flex items-center justify-between">
              <h2 className="text-2xl font-bold text-gray-900">Minhas Reservas</h2>
              <button
                onClick={() => setShowReservationForm(!showReservationForm)}
                className="flex items-center gap-2 bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
              >
                <Plus className="w-4 h-4" />
                Nova Reserva
              </button>
            </div>

            {showReservationForm && (
              <div className="bg-white rounded-xl p-6 border border-gray-200">
                <h3 className="font-semibold text-gray-900 mb-4">Fazer Reserva</h3>
                <form onSubmit={async (e) => {
                  e.preventDefault();
                  const formData = new FormData(e.currentTarget);
                  const tableId = formData.get('tableId') as string;
                  const table = tables.find(t => t.id === tableId);
                  await addReservation({
                    clientName: user?.nome || '',
                    clientPhone: formData.get('phone') as string,
                    tableId: tableId,
                    tableNumber: table ? table.number : 0,
                    date: formData.get('date') as string,
                    time: formData.get('time') as string,
                    guests: parseInt(formData.get('guests') as string),
                    status: 'pending',
                    value: 50.00,
                  });
                  setShowReservationForm(false);
                  e.currentTarget.reset();
                }} className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <input name="phone" type="tel" placeholder="Telefone" className="px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-blue-500" required />
                  <select name="tableId" className="px-4 py-3 border border-gray-300 rounded-lg bg-white outline-none focus:ring-2 focus:ring-blue-500" required>
                    <option value="">Selecione a mesa</option>
                    {tables.filter(t => t.status === 'available').map(table => (
                      <option key={table.id} value={table.id}>
                        Mesa {table.number} - {table.capacity} lugares
                      </option>
                    ))}
                  </select>
                  <input name="date" type="date" className="px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-blue-500" required />
                  <input name="time" type="time" className="px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-blue-500" required />
                  <input name="guests" type="number" min="1" placeholder="Número de pessoas" className="px-4 py-3 border border-gray-300 rounded-lg outline-none focus:ring-2 focus:ring-blue-500" required />
                  <div className="flex gap-2 md:col-span-2">
                    <button type="submit" className="flex-1 bg-green-500 text-white py-3 rounded-lg hover:bg-green-600 transition">
                      Confirmar
                    </button>
                    <button type="button" onClick={() => setShowReservationForm(false)} className="flex-1 bg-gray-300 text-gray-700 py-3 rounded-lg hover:bg-gray-400 transition">
                      Cancelar
                    </button>
                  </div>
                </form>
              </div>
            )}

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {userReservations.map((reservation) => (
                <div key={reservation.id} className="bg-white rounded-xl p-6 border border-gray-200 shadow-sm">
                  <div className="flex items-start justify-between mb-4">
                    <div>
                      <div className="text-lg font-bold text-gray-900">
                        Mesa {reservation.tableNumber}
                      </div>
                      <div className="text-sm text-gray-600">{reservation.guests} pessoas</div>
                    </div>
                    <span className={`px-3 py-1 text-xs rounded-full font-medium ${
                      reservation.status === 'confirmed' ? 'bg-green-100 text-green-700' :
                      reservation.status === 'pending' ? 'bg-yellow-100 text-yellow-700' :
                      reservation.status === 'completed' ? 'bg-blue-100 text-blue-700' :
                      'bg-red-100 text-red-700'
                    }`}>
                      {reservation.status === 'confirmed' ? 'Confirmada' : reservation.status === 'completed' ? 'Finalizada' : 'Cancelada'}
                    </span>
                  </div>
                  <div className="flex items-center gap-4 text-sm text-gray-600">
                    <div className="flex items-center gap-2">
                      <Calendar className="w-4 h-4" />
                      {reservation.date}
                    </div>
                    <div className="flex items-center gap-2">
                      <Clock className="w-4 h-4" />
                      {reservation.time}
                    </div>
                  </div>
                </div>
              ))}
              {userReservations.length === 0 && (
                <div className="col-span-2 text-center py-12 bg-white rounded-xl border border-gray-200 text-gray-500">
                  Você ainda não tem reservas
                </div>
              )}
            </div>
          </div>
        )}

        {activeTab === 'orders' && (
          <div className="space-y-6">
            <h2 className="text-2xl font-bold text-gray-900">Meus Pedidos</h2>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
              {userOrders.map((order) => (
                <div key={order.id} className="bg-white rounded-xl p-6 border border-gray-200 shadow-sm">
                  <div className="flex items-start justify-between mb-4">
                    <div>
                      <div className="text-lg font-bold text-gray-900">Pedido #{order.id}</div>
                      <div className="text-xs text-gray-500 mt-1">
                        {new Date(order.createdAt).toLocaleString('pt-BR')}
                      </div>
                    </div>
                    <div className="text-right">
                      <div className="text-2xl font-bold text-green-600">R$ {order.total.toFixed(2)}</div>
                      <span className={`inline-block px-3 py-1 text-xs rounded-full font-medium mt-2 ${
                        order.status === 'completed' ? 'bg-green-100 text-green-700' :
                        order.status === 'delivered' ? 'bg-blue-100 text-blue-700' :
                        order.status === 'ready' ? 'bg-purple-100 text-purple-700' :
                        order.status === 'preparing' ? 'bg-yellow-100 text-yellow-700' :
                        'bg-gray-100 text-gray-700'
                      }`}>
                        {order.status === 'preparing' ? 'Preparando' : order.status === 'ready' ? 'Pronto' : order.status === 'delivered' ? 'Saiu para Entrega' : order.status === 'completed' ? 'Entregue' : order.status}
                      </span>
                    </div>
                  </div>

                  <div className="border-t border-gray-200 pt-3 space-y-2">
                    {order.items.map((item, idx) => (
                      <div key={idx} className="flex justify-between text-sm">
                        <span className="text-gray-700">{item.quantity}x {item.name}</span>
                        <span className="text-gray-900 font-medium">R$ {(item.price * item.quantity).toFixed(2)}</span>
                      </div>
                    ))}
                  </div>

                  {order.deliveryAddress && (
                    <div className="mt-3 pt-3 border-t border-gray-200 text-sm text-gray-600 flex items-start gap-2">
                      <MapPin className="w-4 h-4 mt-0.5 flex-shrink-0" />
                      {order.deliveryAddress}
                    </div>
                  )}
                </div>
              ))}
              {userOrders.length === 0 && (
                <div className="col-span-2 text-center py-12 bg-white rounded-xl border border-gray-200 text-gray-500">
                  Você ainda não fez nenhum pedido
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
