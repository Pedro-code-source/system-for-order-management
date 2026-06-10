import { useNavigate } from 'react-router';
import { Home, AlertCircle } from 'lucide-react';

export function NotFound() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-50 to-gray-100 flex items-center justify-center p-6">
      <div className="text-center">
        <AlertCircle className="w-20 h-20 text-orange-500 mx-auto mb-6" />
        <h1 className="text-6xl font-bold text-gray-900 mb-4">404</h1>
        <p className="text-xl text-gray-600 mb-8">Página não encontrada</p>
        <button
          onClick={() => navigate('/')}
          className="inline-flex items-center gap-2 bg-orange-500 text-white px-6 py-3 rounded-lg font-semibold hover:bg-orange-600 transition"
        >
          <Home className="w-5 h-5" />
          Voltar ao Início
        </button>
      </div>
    </div>
  );
}
