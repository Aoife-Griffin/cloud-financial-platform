import { useState, useEffect } from 'react';
import API from '../services/api';

interface Transaction {
  id: number;
  accountId: number;
  categoryName: string;
  amount: number;
  type: string;
  description: string;
  transactionDate: string;
}

export const Transactions = () => {
  const [search, setSearch] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('');
  const [sortBy, setSortBy] = useState<'date' | 'amount'>('date');
  const [page, setPage] = useState(0); /// Originally uset state 1 but set for 0 indexing
  const [pageSize] = useState(10);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [totalPages, setTotalPages] = useState(1);

  /// Get logs every time the page mutates
  useEffect(() => {
    fetchTransactions();
  }, [page, categoryFilter]);

  const fetchTransactions = async () => {
    try {
      
      const response = await API.get('/transactions', {
        params: {
          page: page,
          size: pageSize,
          category: categoryFilter || undefined
        }
      });

      setTransactions(response.data.content);
      setTotalPages(response.data.totalPages || 1);
    }  catch (err) {
      console.error('Failed to stream paginated transactional ledger matrix logs:', err);
    }
  };
  const filteredData = [...transactions]
    .filter(t => t.description?.toLowerCase().includes(search.toLowerCase()))
    .sort((a, b) => {
      if (sortBy === 'date') {
        return new Date(b.transactionDate).getTime() - new Date(a.transactionDate).getTime();
      }
      return b.amount - a.amount;
    });

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-3xl font-bold text-gray-900">Transactions</h2>
        <button className="px-4 py-2 bg-blue-600 text-white font-medium rounded hover:bg-blue-700 transition">
          + Add Transaction
        </button>
      </div>


      {/* Filterinf Interface */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 bg-white p-4 shadow rounded-lg">
        <input type="text" placeholder="Search description..." className="p-2 border rounded focus:ring-2 focus:ring-blue-500" value={search} onChange={e => setSearch(e.target.value)} />
        <select className="p-2 border rounded" value={categoryFilter} onChange={e => setCategoryFilter(e.target.value)}>
          <option value="">All Categories</option>
          <option value="Food">Food</option>
          <option value="Transport">Transport</option>
          <option value="Income">Income</option>
        </select>
        <select className="p-2 border rounded" value={sortBy} onChange={e => setSortBy(e.target.value as any)}>
          <option value="date">Sort by Date</option>
          <option value="amount">Sort by Amount</option>
        </select>
      </div>

      {/* Presenting the matrix */}
      <div className="bg-white shadow rounded-lg overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Description</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Category</th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase">Amount</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200 bg-white">
            {filteredData.map(t => (
              <tr key={t.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {new Date(t.transactionDate).toLocaleDateString()}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{t.description}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{t.categoryName || 'Uncategorized'}</td>
                <td className={`px-6 py-4 whitespace-nowrap text-sm text-right font-semibold ${t.type === 'CREDIT' ? 'text-green-600' : 'text-red-600'}`}>
                  {t.type === 'CREDIT' ? `+€${t.amount}` : `-€${t.amount}`}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>


      {/* setting up pagination */}
      <div className="flex justify-between items-center bg-white p-4 shadow rounded-lg">
        <button 
          disabled={page === 0} 
          onClick={() => setPage(p => Math.max(0, p - 1))} 
          className="px-3 py-1 border rounded disabled:opacity-50"
        >
          Previous
        </button>
        <span className="text-sm text-gray-650">Page {page + 1} of {totalPages}</span>
        <button 
          disabled={page >= totalPages - 1} 
          onClick={() => setPage(p => p + 1)} 
          className="px-3 py-1 border rounded disabled:opacity-50"
        >
          Next
        </button>
      </div>
    </div>
  );
};
