import { Link, Outlet, useNavigate } from 'react-router-dom';

/// Sidebar that has links and logout
export const Layout = () => {
  const navigate = useNavigate();
  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };
  

  return (
    <div className="flex h-screen bg-gray-100 font-sans">
      <aside className="w-64 bg-slate-900 text-white flex flex-col justify-between">
        <div className="p-5">
          <h1 className="text-xl font-bold tracking-tight mb-8">FintechApp</h1>
          <nav className="space-y-2">
            <Link to="/dashboard" className="block p-3 hover:bg-slate-850 rounded transition">Dashboard</Link>
            <Link to="/accounts" className="block p-3 hover:bg-slate-850 rounded transition">Accounts</Link>
            <Link to="/transactions" className="block p-3 hover:bg-slate-850 rounded transition">Transactions</Link>
            <Link to="/budgets" className="block p-3 hover:bg-slate-850 rounded transition">Budgets</Link>
          </nav>
        </div>
        <button onClick={handleLogout} className="m-5 p-3 bg-red-600 hover:bg-red-750 text-white font-medium rounded transition">
          Log Out
        </button>
      </aside>
      <main className="flex-1 p-8 overflow-y-auto">
        <Outlet />
      </main>
    </div>
  );
};
