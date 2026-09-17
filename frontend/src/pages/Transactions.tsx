import { useState } from 'react';

interface Transaction {
  id: number;
  date: string;
  description: string;
  category: string;
  amount: number;
}

export const Transactions = () => {
  const [search, setSearch] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('');
  const [sortBy, setSortBy] = useState<'date' | 'amount'>('date');
  const [page, setPage] = useState(1);

  const [transactions] = useState<Transaction[]>([
    { id: 1, date: '02/09', description: 'Tesco', category: 'Food', amount: -45 },
    { id: 2, date: '03/09', description: 'Salary', category: 'Income', amount: 2500 },
    { id: 3, date: '04/09', description: 'Bus', category: 'Transport', amount: -12 },
  ]);

  /// Filtering by what user inputs
  const filteredData = transactions
    .filter(t => t.description.toLowerCase().includes(search.toLowerCase()))
    .filter(t => (categoryFilter ? t.category === categoryFilter : true))
    .sort((a, b) => {
      if (sortBy === 'date') return b.date.localeCompare(a.date);
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
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{t.date}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{t.description}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{t.category}</td>
                <td className={`px-6 py-4 whitespace-nowrap text-sm text-right font-semibold ${t.amount > 0 ? 'text-green-600' : 'text-red-600'}`}>
                  {t.amount > 0 ? `+€${t.amount}` : `-€${Math.abs(t.amount)}`}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>


      {/* setting up pagination */}
      <div className="flex justify-between items-center bg-white p-4 shadow rounded-lg">
        <button disabled={page === 1} onClick={() => setPage(p => p - 1)} className="px-3 py-1 border rounded disabled:opacity-50">Previous</button>
        <span className="text-sm text-gray-650">Page {page}</span>
        <button onClick={() => setPage(p => p + 1)} className="px-3 py-1 border rounded">Next</button>
      </div>
    </div>
  );
};
