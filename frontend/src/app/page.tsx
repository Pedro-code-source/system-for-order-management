"use client"
<<<<<<< Updated upstream

import ClientLayout from "../../components/layout/ClientLayout";
import DashboardLayout from "../../components/dashboard/DashboardLayout";
import DashboardCard from "../../components/dashboard/DashboardCard";
import Login from "../../components/login/page";
import Cadastro from "../../components/cadastro/page";
=======
import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '../contexts/AuthContext';
import { ChefHat, Mail, Lock, UserCircle, UtensilsCrossed } from 'lucide-react';
import Link from 'next/link';
>>>>>>> Stashed changes

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { login, user } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (user) {
      router.push(`/${user.role}`);
    }
  }, [user, router]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsSubmitting(true);

    try {
      const success = await login(email, password);
      if (!success) {
        setError('Credenciais inválidas. Tente novamente.');
      }
    } catch (err: any) {
      setError(err.message || 'Erro ao realizar login.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const quickLogin = (role: 'admin' | 'client' | 'waiter') => {
    const credentials = {
      admin: { email: 'admin@restaurant.com', password: 'admin123' },
      client: { email: 'cliente@email.com', password: 'cliente123' },
      waiter: { email: 'garcom@restaurant.com', password: 'garcom123' },
    };
    setEmail(credentials[role].email);
    setPassword(credentials[role].password);
  };

  return (
<<<<<<< Updated upstream
    <Login></Login>
=======
    <div className="min-h-screen bg-gradient-to-br from-orange-50 via-white to-red-50 flex items-center justify-center p-6">
      <div className="max-w-md w-full">
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-20 h-20 bg-gradient-to-br from-orange-500 to-red-500 rounded-2xl mb-4 shadow-lg animate-bounce">
            <ChefHat className="w-10 h-10 text-white" />
          </div>
          <h1 className="text-4xl font-bold text-gray-900 mb-2">Restaurante System</h1>
          <p className="text-gray-600">Sistema Completo de Gerenciamento</p>
        </div>

        <div className="bg-white rounded-2xl shadow-xl p-8 border border-gray-100">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Email
              </label>
              <div className="relative">
                <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full pl-11 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent outline-none transition"
                  placeholder="seu@email.com"
                  required
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Senha
              </label>
              <div className="relative">
                <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full pl-11 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-transparent outline-none transition"
                  placeholder="••••••••"
                  required
                />
              </div>
            </div>

            {error && (
              <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm">
                {error}
              </div>
            )}

            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full bg-gradient-to-r from-orange-500 to-red-500 text-white py-3 rounded-lg font-semibold hover:from-orange-600 hover:to-red-600 transition shadow-lg hover:shadow-xl disabled:opacity-50"
            >
              {isSubmitting ? 'Entrando...' : 'Entrar'}
            </button>
          </form>

          <div className="mt-4 text-center">
            <Link href="/cadastro" className="text-sm text-orange-600 hover:text-orange-700 font-medium">
              Não tem uma conta? Cadastre-se aqui
            </Link>
          </div>

          <div className="mt-6 pt-6 border-t border-gray-200">
            <p className="text-sm text-gray-600 text-center mb-4">Acesso rápido para demonstração:</p>
            <div className="grid grid-cols-3 gap-3">
              <button
                onClick={() => quickLogin('admin')}
                className="flex flex-col items-center gap-2 p-3 bg-purple-50 hover:bg-purple-100 rounded-lg transition border border-purple-200"
              >
                <UserCircle className="w-6 h-6 text-purple-600" />
                <span className="text-xs font-medium text-purple-900">Admin</span>
              </button>
              <button
                onClick={() => quickLogin('client')}
                className="flex flex-col items-center gap-2 p-3 bg-blue-50 hover:bg-blue-100 rounded-lg transition border border-blue-200"
              >
                <UtensilsCrossed className="w-6 h-6 text-blue-600" />
                <span className="text-xs font-medium text-blue-900">Cliente</span>
              </button>
              <button
                onClick={() => quickLogin('waiter')}
                className="flex flex-col items-center gap-2 p-3 bg-green-50 hover:bg-green-100 rounded-lg transition border border-green-200"
              >
                <ChefHat className="w-6 h-6 text-green-600" />
                <span className="text-xs font-medium text-green-900">Garçom</span>
              </button>
            </div>
          </div>
        </div>

        <div className="mt-6 text-center text-sm text-gray-500">
          <p>Sistema de gerenciamento completo para restaurantes</p>
        </div>
      </div>
    </div>
>>>>>>> Stashed changes
  );
}
