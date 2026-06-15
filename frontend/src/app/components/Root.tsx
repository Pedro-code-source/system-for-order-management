import { Outlet } from 'react-router';
import { AuthProvider } from '../context/AuthContext';
import { RestaurantProvider } from '../context/RestaurantContext';

export function Root() {
  return (
    <AuthProvider>
      <RestaurantProvider>
        <Outlet />
      </RestaurantProvider>
    </AuthProvider>
  );
}
