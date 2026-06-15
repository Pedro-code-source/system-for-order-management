import { createBrowserRouter } from "react-router";
import { Root } from "./components/Root";
import { Login } from "./components/Login";
import { AdminDashboard } from "./components/admin/AdminDashboard";
import { ClientDashboard } from "./components/client/ClientDashboard";
import { WaiterDashboard } from "./components/waiter/WaiterDashboard";
import { NotFound } from "./components/NotFound";

export const router = createBrowserRouter([
  {
    path: "/",
    Component: Root,
    children: [
      { index: true, Component: Login },
      { path: "admin/*", Component: AdminDashboard },
      { path: "client/*", Component: ClientDashboard },
      { path: "waiter/*", Component: WaiterDashboard },
      { path: "*", Component: NotFound },
    ],
  },
]);
