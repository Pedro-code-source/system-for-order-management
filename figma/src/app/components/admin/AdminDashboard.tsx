import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import { useAuth } from '../../context/AuthContext';
import { useRestaurant } from '../../context/RestaurantContext';
import {
  LayoutDashboard, UtensilsCrossed, Users, Package, TrendingUp,
  Calendar, ShoppingCart, Truck, LogOut, Settings, Plus, Edit2,
  Trash2, Check, X, AlertTriangle, ChefHat, DollarSign
} from 'lucide-react';

type Tab = 'overview' | 'menu' | 'tables' | 'reservations' | 'orders' | 'inventory' | 'delivery';

export function AdminDashboard() {
  const [activeTab, setActiveTab] = useState<Tab>('overview');
  const [showAddMenu, setShowAddMenu] = useState(false);
  const [showAddTable, setShowAddTable] = useState(false);
  const [showAddIngredient, setShowAddIngredient] = useState(false);
  const [showStockMovement, setShowStockMovement] = useState(false);
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const {
    menuItems, tables, reservations, orders, ingredients, stockMovements,
    addMenuItem, updateMenuItem, deleteMenuItem,
    addTable, updateTable,
    updateReservation, updateOrder,
    addIngredient, updateIngredient, addStockMovement
  } = useRestaurant();

  useEffect(() => {
    if (!user || user.role !== 'admin') {
      navigate('/');
    }
  }, [user, navigate]);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const stats = {
    totalOrders: orders.length,
    pendingOrders: orders.filter(o => o.status === 'pending').length,
    totalRevenue: orders.reduce((sum, o) => sum + o.total, 0),
    activeReservations: reservations.filter(r => r.status === 'confirmed').length,
    lowStockItems: ingredients.filter(i => i.quantity < i.minStock).length,
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200 sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-6 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="bg-purple-500 p-2 rounded-lg">
                <ChefHat className="w-6 h-6 text-white" />
              </div>
              <div>
                <h1 className="text-xl font-bold text-gray-900">Painel Administrativo</h1>
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
            { id: 'overview', label: 'Visão Geral', icon: LayoutDashboard },
            { id: 'menu', label: 'Cardápio', icon: UtensilsCrossed },
            { id: 'tables', label: 'Mesas', icon: Users },
            { id: 'reservations', label: 'Reservas', icon: Calendar },
            { id: 'orders', label: 'Pedidos', icon: ShoppingCart },
            { id: 'inventory', label: 'Estoque', icon: Package },
            { id: 'delivery', label: 'Entregas', icon: Truck },
          ].map((tab) => {
            const Icon = tab.icon;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id as Tab)}
                className={`flex items-center gap-2 px-4 py-2 rounded-lg font-medium transition whitespace-nowrap ${
                  activeTab === tab.id
                    ? 'bg-purple-500 text-white shadow-lg'
                    : 'bg-white text-gray-700 hover:bg-gray-100'
                }`}
              >
                <Icon className="w-4 h-4" />
                {tab.label}
              </button>
            );
          })}
        </div>

        {activeTab === 'overview' && (
          <div className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
              <div className="bg-white p-6 rounded-xl border border-gray-200">
                <div className="flex items-center justify-between mb-2">
                  <div className="bg-blue-100 p-2 rounded-lg">
                    <ShoppingCart className="w-5 h-5 text-blue-600" />
                  </div>
                  <span className="text-sm text-gray-500">Total</span>
                </div>
                <div className="text-3xl font-bold text-gray-900">{stats.totalOrders}</div>
                <div className="text-sm text-gray-600 mt-1">Pedidos</div>
              </div>

              <div className="bg-white p-6 rounded-xl border border-gray-200">
                <div className="flex items-center justify-between mb-2">
                  <div className="bg-green-100 p-2 rounded-lg">
                    <DollarSign className="w-5 h-5 text-green-600" />
                  </div>
                  <span className="text-sm text-gray-500">Receita</span>
                </div>
                <div className="text-3xl font-bold text-gray-900">
                  R$ {stats.totalRevenue.toFixed(2)}
                </div>
                <div className="text-sm text-gray-600 mt-1">Total</div>
              </div>

              <div className="bg-white p-6 rounded-xl border border-gray-200">
                <div className="flex items-center justify-between mb-2">
                  <div className="bg-purple-100 p-2 rounded-lg">
                    <Calendar className="w-5 h-5 text-purple-600" />
                  </div>
                  <span className="text-sm text-gray-500">Ativas</span>
                </div>
                <div className="text-3xl font-bold text-gray-900">{stats.activeReservations}</div>
                <div className="text-sm text-gray-600 mt-1">Reservas</div>
              </div>

              <div className="bg-white p-6 rounded-xl border border-gray-200">
                <div className="flex items-center justify-between mb-2">
                  <div className="bg-orange-100 p-2 rounded-lg">
                    <AlertTriangle className="w-5 h-5 text-orange-600" />
                  </div>
                  <span className="text-sm text-gray-500">Alerta</span>
                </div>
                <div className="text-3xl font-bold text-gray-900">{stats.lowStockItems}</div>
                <div className="text-sm text-gray-600 mt-1">Estoque Baixo</div>
              </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div className="bg-white rounded-xl border border-gray-200 p-6">
                <h3 className="text-lg font-semibold text-gray-900 mb-4">Pedidos Recentes</h3>
                <div className="space-y-3">
                  {orders.slice(0, 5).map((order) => (
                    <div key={order.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                      <div>
                        <div className="font-medium text-gray-900">#{order.id}</div>
                        <div className="text-sm text-gray-600">{order.clientName}</div>
                      </div>
                      <div className="text-right">
                        <div className="font-semibold text-gray-900">R$ {order.total.toFixed(2)}</div>
                        <div className={`text-xs px-2 py-1 rounded-full inline-block ${
                          order.status === 'completed' ? 'bg-green-100 text-green-700' :
                          order.status === 'preparing' ? 'bg-blue-100 text-blue-700' :
                          'bg-yellow-100 text-yellow-700'
                        }`}>
                          {order.status}
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>

              <div className="bg-white rounded-xl border border-gray-200 p-6">
                <h3 className="text-lg font-semibold text-gray-900 mb-4">Itens em Estoque Baixo</h3>
                <div className="space-y-3">
                  {ingredients.filter(i => i.quantity < i.minStock).map((ingredient) => (
                    <div key={ingredient.id} className="flex items-center justify-between p-3 bg-red-50 rounded-lg border border-red-200">
                      <div>
                        <div className="font-medium text-gray-900">{ingredient.name}</div>
                        <div className="text-sm text-gray-600">Mínimo: {ingredient.minStock} {ingredient.unit}</div>
                      </div>
                      <div className="text-right">
                        <div className="font-semibold text-red-600">{ingredient.quantity} {ingredient.unit}</div>
                        <div className="text-xs text-red-600">Reabastecer</div>
                      </div>
                    </div>
                  ))}
                  {ingredients.filter(i => i.quantity < i.minStock).length === 0 && (
                    <div className="text-center py-8 text-gray-500">
                      <Check className="w-12 h-12 mx-auto mb-2 text-green-500" />
                      <p>Todos os itens com estoque adequado</p>
                    </div>
                  )}
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'menu' && (
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Gerenciar Cardápio</h2>
              <button
                onClick={() => setShowAddMenu(!showAddMenu)}
                className="flex items-center gap-2 bg-purple-500 text-white px-4 py-2 rounded-lg hover:bg-purple-600 transition"
              >
                <Plus className="w-4 h-4" />
                Adicionar Item
              </button>
            </div>

            {showAddMenu && (
              <div className="bg-white p-6 rounded-xl border border-gray-200">
                <h3 className="font-semibold text-gray-900 mb-4">Novo Item do Cardápio</h3>
                <form onSubmit={(e) => {
                  e.preventDefault();
                  const formData = new FormData(e.currentTarget);
                  addMenuItem({
                    name: formData.get('name') as string,
                    category: formData.get('category') as string,
                    price: parseFloat(formData.get('price') as string),
                    description: formData.get('description') as string,
                    image: formData.get('image') as string,
                    available: true,
                  });
                  setShowAddMenu(false);
                  e.currentTarget.reset();
                }} className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <input name="name" placeholder="Nome do prato" className="px-4 py-2 border border-gray-300 rounded-lg" required />
                  <input name="category" placeholder="Categoria" className="px-4 py-2 border border-gray-300 rounded-lg" required />
                  <input name="price" type="number" step="0.01" placeholder="Preço" className="px-4 py-2 border border-gray-300 rounded-lg" required />
                  <input name="image" placeholder="URL da imagem" className="px-4 py-2 border border-gray-300 rounded-lg" required />
                  <input name="description" placeholder="Descrição" className="px-4 py-2 border border-gray-300 rounded-lg md:col-span-2" required />
                  <div className="md:col-span-2 flex gap-2">
                    <button type="submit" className="flex-1 bg-green-500 text-white py-2 rounded-lg hover:bg-green-600">
                      Salvar
                    </button>
                    <button type="button" onClick={() => setShowAddMenu(false)} className="flex-1 bg-gray-300 text-gray-700 py-2 rounded-lg hover:bg-gray-400">
                      Cancelar
                    </button>
                  </div>
                </form>
              </div>
            )}

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {menuItems.map((item) => (
                <div key={item.id} className="bg-white rounded-xl border border-gray-200 overflow-hidden">
                  <img src={item.image} alt={item.name} className="w-full h-40 object-cover" />
                  <div className="p-4">
                    <div className="flex items-start justify-between mb-2">
                      <div>
                        <h3 className="font-semibold text-gray-900">{item.name}</h3>
                        <p className="text-sm text-gray-600">{item.category}</p>
                      </div>
                      <div className="text-lg font-bold text-green-600">
                        R$ {item.price.toFixed(2)}
                      </div>
                    </div>
                    <p className="text-sm text-gray-600 mb-3">{item.description}</p>
                    <div className="flex gap-2">
                      <button
                        onClick={() => updateMenuItem(item.id, { available: !item.available })}
                        className={`flex-1 py-2 rounded-lg text-sm font-medium transition ${
                          item.available ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-700'
                        }`}
                      >
                        {item.available ? 'Disponível' : 'Indisponível'}
                      </button>
                      <button
                        onClick={() => deleteMenuItem(item.id)}
                        className="px-3 py-2 bg-red-100 text-red-600 rounded-lg hover:bg-red-200 transition"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === 'tables' && (
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Gerenciar Mesas</h2>
              <button
                onClick={() => setShowAddTable(!showAddTable)}
                className="flex items-center gap-2 bg-purple-500 text-white px-4 py-2 rounded-lg hover:bg-purple-600 transition"
              >
                <Plus className="w-4 h-4" />
                Adicionar Mesa
              </button>
            </div>

            {showAddTable && (
              <div className="bg-white p-6 rounded-xl border border-gray-200">
                <h3 className="font-semibold text-gray-900 mb-4">Nova Mesa</h3>
                <form onSubmit={(e) => {
                  e.preventDefault();
                  const formData = new FormData(e.currentTarget);
                  addTable({
                    number: parseInt(formData.get('number') as string),
                    capacity: parseInt(formData.get('capacity') as string),
                    status: 'available',
                  });
                  setShowAddTable(false);
                  e.currentTarget.reset();
                }} className="flex gap-4">
                  <input name="number" type="number" placeholder="Número" className="px-4 py-2 border border-gray-300 rounded-lg" required />
                  <input name="capacity" type="number" placeholder="Capacidade" className="px-4 py-2 border border-gray-300 rounded-lg" required />
                  <button type="submit" className="bg-green-500 text-white px-6 py-2 rounded-lg hover:bg-green-600">
                    Salvar
                  </button>
                  <button type="button" onClick={() => setShowAddTable(false)} className="bg-gray-300 text-gray-700 px-6 py-2 rounded-lg hover:bg-gray-400">
                    Cancelar
                  </button>
                </form>
              </div>
            )}

            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
              {tables.map((table) => (
                <div
                  key={table.id}
                  className={`p-6 rounded-xl border-2 ${
                    table.status === 'available' ? 'bg-green-50 border-green-200' :
                    table.status === 'occupied' ? 'bg-red-50 border-red-200' :
                    'bg-yellow-50 border-yellow-200'
                  }`}
                >
                  <div className="text-center">
                    <Users className={`w-8 h-8 mx-auto mb-2 ${
                      table.status === 'available' ? 'text-green-600' :
                      table.status === 'occupied' ? 'text-red-600' :
                      'text-yellow-600'
                    }`} />
                    <div className="text-2xl font-bold text-gray-900 mb-1">Mesa {table.number}</div>
                    <div className="text-sm text-gray-600 mb-3">{table.capacity} lugares</div>
                    <select
                      value={table.status}
                      onChange={(e) => updateTable(table.id, { status: e.target.value as any })}
                      className="w-full px-3 py-2 border border-gray-300 rounded-lg text-sm"
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

        {activeTab === 'reservations' && (
          <div className="space-y-4">
            <h2 className="text-2xl font-bold text-gray-900">Gerenciar Reservas</h2>
            <div className="bg-white rounded-xl border border-gray-200 overflow-hidden">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Cliente</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Telefone</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Data/Hora</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Pessoas</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Mesa</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Ações</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {reservations.map((reservation) => (
                    <tr key={reservation.id}>
                      <td className="px-6 py-4 text-sm text-gray-900">{reservation.clientName}</td>
                      <td className="px-6 py-4 text-sm text-gray-600">{reservation.clientPhone}</td>
                      <td className="px-6 py-4 text-sm text-gray-600">{reservation.date} {reservation.time}</td>
                      <td className="px-6 py-4 text-sm text-gray-600">{reservation.guests}</td>
                      <td className="px-6 py-4 text-sm text-gray-600">
                        Mesa {tables.find(t => t.id === reservation.tableId)?.number}
                      </td>
                      <td className="px-6 py-4">
                        <span className={`px-2 py-1 text-xs rounded-full ${
                          reservation.status === 'confirmed' ? 'bg-green-100 text-green-700' :
                          reservation.status === 'pending' ? 'bg-yellow-100 text-yellow-700' :
                          reservation.status === 'completed' ? 'bg-blue-100 text-blue-700' :
                          'bg-red-100 text-red-700'
                        }`}>
                          {reservation.status}
                        </span>
                      </td>
                      <td className="px-6 py-4">
                        <div className="flex gap-2">
                          {reservation.status === 'pending' && (
                            <button
                              onClick={() => updateReservation(reservation.id, { status: 'confirmed' })}
                              className="p-1 bg-green-100 text-green-600 rounded hover:bg-green-200"
                            >
                              <Check className="w-4 h-4" />
                            </button>
                          )}
                          <button
                            onClick={() => updateReservation(reservation.id, { status: 'cancelled' })}
                            className="p-1 bg-red-100 text-red-600 rounded hover:bg-red-200"
                          >
                            <X className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {reservations.length === 0 && (
                <div className="text-center py-12 text-gray-500">
                  Nenhuma reserva encontrada
                </div>
              )}
            </div>
          </div>
        )}

        {activeTab === 'orders' && (
          <div className="space-y-4">
            <h2 className="text-2xl font-bold text-gray-900">Gerenciar Pedidos</h2>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
              {orders.map((order) => (
                <div key={order.id} className="bg-white rounded-xl border border-gray-200 p-6">
                  <div className="flex items-start justify-between mb-4">
                    <div>
                      <div className="text-lg font-bold text-gray-900">Pedido #{order.id}</div>
                      <div className="text-sm text-gray-600">{order.clientName}</div>
                      <div className="text-xs text-gray-500 mt-1">
                        {order.type === 'online' ? '🏠 Online' : '🍽️ Presencial'}
                      </div>
                    </div>
                    <div className="text-right">
                      <div className="text-2xl font-bold text-green-600">R$ {order.total.toFixed(2)}</div>
                      <div className="text-xs text-gray-500 mt-1">{order.paymentMethod}</div>
                    </div>
                  </div>

                  <div className="border-t border-gray-200 pt-3 mb-3">
                    {order.items.map((item, idx) => (
                      <div key={idx} className="flex justify-between text-sm py-1">
                        <span className="text-gray-700">{item.quantity}x {item.name}</span>
                        <span className="text-gray-900 font-medium">R$ {(item.price * item.quantity).toFixed(2)}</span>
                      </div>
                    ))}
                  </div>

                  <div className="flex gap-2">
                    <select
                      value={order.status}
                      onChange={(e) => updateOrder(order.id, { status: e.target.value as any })}
                      className="flex-1 px-3 py-2 border border-gray-300 rounded-lg text-sm"
                    >
                      <option value="pending">Pendente</option>
                      <option value="preparing">Preparando</option>
                      <option value="ready">Pronto</option>
                      <option value="delivered">Entregue</option>
                      <option value="completed">Concluído</option>
                    </select>
                  </div>
                </div>
              ))}
              {orders.length === 0 && (
                <div className="col-span-2 text-center py-12 text-gray-500 bg-white rounded-xl border border-gray-200">
                  Nenhum pedido encontrado
                </div>
              )}
            </div>
          </div>
        )}

        {activeTab === 'inventory' && (
          <div className="space-y-4">
            <div className="flex justify-between items-center">
              <h2 className="text-2xl font-bold text-gray-900">Controle de Estoque</h2>
              <div className="flex gap-2">
                <button
                  onClick={() => setShowStockMovement(!showStockMovement)}
                  className="flex items-center gap-2 bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
                >
                  <TrendingUp className="w-4 h-4" />
                  Movimentação
                </button>
                <button
                  onClick={() => setShowAddIngredient(!showAddIngredient)}
                  className="flex items-center gap-2 bg-purple-500 text-white px-4 py-2 rounded-lg hover:bg-purple-600 transition"
                >
                  <Plus className="w-4 h-4" />
                  Adicionar Item
                </button>
              </div>
            </div>

            {showAddIngredient && (
              <div className="bg-white p-6 rounded-xl border border-gray-200">
                <h3 className="font-semibold text-gray-900 mb-4">Novo Ingrediente</h3>
                <form onSubmit={(e) => {
                  e.preventDefault();
                  const formData = new FormData(e.currentTarget);
                  addIngredient({
                    name: formData.get('name') as string,
                    quantity: parseFloat(formData.get('quantity') as string),
                    unit: formData.get('unit') as string,
                    minStock: parseFloat(formData.get('minStock') as string),
                  });
                  setShowAddIngredient(false);
                  e.currentTarget.reset();
                }} className="grid grid-cols-1 md:grid-cols-4 gap-4">
                  <input name="name" placeholder="Nome" className="px-4 py-2 border border-gray-300 rounded-lg" required />
                  <input name="quantity" type="number" step="0.1" placeholder="Quantidade" className="px-4 py-2 border border-gray-300 rounded-lg" required />
                  <input name="unit" placeholder="Unidade (kg, L, un)" className="px-4 py-2 border border-gray-300 rounded-lg" required />
                  <input name="minStock" type="number" step="0.1" placeholder="Estoque Mínimo" className="px-4 py-2 border border-gray-300 rounded-lg" required />
                  <div className="md:col-span-4 flex gap-2">
                    <button type="submit" className="flex-1 bg-green-500 text-white py-2 rounded-lg hover:bg-green-600">
                      Salvar
                    </button>
                    <button type="button" onClick={() => setShowAddIngredient(false)} className="flex-1 bg-gray-300 text-gray-700 py-2 rounded-lg hover:bg-gray-400">
                      Cancelar
                    </button>
                  </div>
                </form>
              </div>
            )}

            {showStockMovement && (
              <div className="bg-white p-6 rounded-xl border border-gray-200">
                <h3 className="font-semibold text-gray-900 mb-4">Registrar Movimentação</h3>
                <form onSubmit={(e) => {
                  e.preventDefault();
                  const formData = new FormData(e.currentTarget);
                  addStockMovement({
                    ingredientId: formData.get('ingredientId') as string,
                    type: formData.get('type') as 'in' | 'out',
                    quantity: parseFloat(formData.get('quantity') as string),
                    date: new Date().toISOString(),
                    reason: formData.get('reason') as string,
                  });
                  setShowStockMovement(false);
                  e.currentTarget.reset();
                }} className="grid grid-cols-1 md:grid-cols-4 gap-4">
                  <select name="ingredientId" className="px-4 py-2 border border-gray-300 rounded-lg" required>
                    <option value="">Selecione o ingrediente</option>
                    {ingredients.map(i => (
                      <option key={i.id} value={i.id}>{i.name}</option>
                    ))}
                  </select>
                  <select name="type" className="px-4 py-2 border border-gray-300 rounded-lg" required>
                    <option value="in">Entrada</option>
                    <option value="out">Saída</option>
                  </select>
                  <input name="quantity" type="number" step="0.1" placeholder="Quantidade" className="px-4 py-2 border border-gray-300 rounded-lg" required />
                  <input name="reason" placeholder="Motivo" className="px-4 py-2 border border-gray-300 rounded-lg" required />
                  <div className="md:col-span-4 flex gap-2">
                    <button type="submit" className="flex-1 bg-green-500 text-white py-2 rounded-lg hover:bg-green-600">
                      Registrar
                    </button>
                    <button type="button" onClick={() => setShowStockMovement(false)} className="flex-1 bg-gray-300 text-gray-700 py-2 rounded-lg hover:bg-gray-400">
                      Cancelar
                    </button>
                  </div>
                </form>
              </div>
            )}

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {ingredients.map((ingredient) => (
                <div
                  key={ingredient.id}
                  className={`p-6 rounded-xl border-2 ${
                    ingredient.quantity < ingredient.minStock
                      ? 'bg-red-50 border-red-200'
                      : 'bg-white border-gray-200'
                  }`}
                >
                  <div className="flex items-start justify-between mb-4">
                    <div>
                      <h3 className="font-semibold text-gray-900">{ingredient.name}</h3>
                      <p className="text-sm text-gray-600">Mínimo: {ingredient.minStock} {ingredient.unit}</p>
                    </div>
                    {ingredient.quantity < ingredient.minStock && (
                      <AlertTriangle className="w-5 h-5 text-red-500" />
                    )}
                  </div>
                  <div className="text-3xl font-bold text-gray-900 mb-1">
                    {ingredient.quantity} <span className="text-lg text-gray-600">{ingredient.unit}</span>
                  </div>
                  <div className="mt-3 flex gap-2">
                    <input
                      type="number"
                      step="0.1"
                      placeholder="Ajustar"
                      className="flex-1 px-3 py-2 border border-gray-300 rounded-lg text-sm"
                      onKeyDown={(e) => {
                        if (e.key === 'Enter') {
                          const value = parseFloat((e.target as HTMLInputElement).value);
                          if (value) {
                            updateIngredient(ingredient.id, { quantity: ingredient.quantity + value });
                            (e.target as HTMLInputElement).value = '';
                          }
                        }
                      }}
                    />
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {activeTab === 'delivery' && (
          <div className="space-y-4">
            <h2 className="text-2xl font-bold text-gray-900">Gerenciar Entregas</h2>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
              {orders.filter(o => o.type === 'online').map((order) => (
                <div key={order.id} className="bg-white rounded-xl border border-gray-200 p-6">
                  <div className="flex items-start justify-between mb-4">
                    <div>
                      <div className="text-lg font-bold text-gray-900">Pedido #{order.id}</div>
                      <div className="text-sm text-gray-600">{order.clientName}</div>
                      <div className="text-xs text-gray-500 mt-1">
                        <Truck className="w-3 h-3 inline mr-1" />
                        {order.deliveryAddress}
                      </div>
                    </div>
                    <div className="text-right">
                      <div className="text-2xl font-bold text-green-600">R$ {order.total.toFixed(2)}</div>
                    </div>
                  </div>

                  <div className="border-t border-gray-200 pt-3 mb-3">
                    {order.items.map((item, idx) => (
                      <div key={idx} className="flex justify-between text-sm py-1">
                        <span className="text-gray-700">{item.quantity}x {item.name}</span>
                        <span className="text-gray-900 font-medium">R$ {(item.price * item.quantity).toFixed(2)}</span>
                      </div>
                    ))}
                  </div>

                  <div className="flex gap-2">
                    <select
                      value={order.status}
                      onChange={(e) => updateOrder(order.id, { status: e.target.value as any })}
                      className="flex-1 px-3 py-2 border border-gray-300 rounded-lg text-sm"
                    >
                      <option value="pending">Aguardando</option>
                      <option value="preparing">Preparando</option>
                      <option value="ready">Saiu para entrega</option>
                      <option value="delivered">Entregue</option>
                    </select>
                  </div>
                </div>
              ))}
              {orders.filter(o => o.type === 'online').length === 0 && (
                <div className="col-span-2 text-center py-12 text-gray-500 bg-white rounded-xl border border-gray-200">
                  Nenhuma entrega pendente
                </div>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
