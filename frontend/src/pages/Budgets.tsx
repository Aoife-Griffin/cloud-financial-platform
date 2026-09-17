export const Budgets = () => {
  const budgets = [
    { category: 'Food', spent: 325, limit: 400 },
  ];

  return (
    <div className="space-y-6 max-w-2xl">
      <h2 className="text-3xl font-bold text-gray-900">Budgets</h2>
      {budgets.map((b, idx) => {
        const percent = Math.min((b.spent / b.limit) * 100, 100);
        const remaining = b.limit - b.spent;

        return (
          <div key={idx} className="bg-white p-6 shadow rounded-lg space-y-4">
            <div className="flex justify-between items-baseline">
              <h3 className="text-xl font-bold text-gray-900">{b.category}</h3>
              <span className="text-sm text-gray-500">
                <strong className="text-gray-900 text-base">€{b.spent}</strong> / €{b.limit}
              </span>
            </div>
            
            <div className="w-full bg-gray-200 rounded-full h-3">
              <div className="bg-blue-600 h-3 rounded-full" style={{ width: `${percent}%` }}></div>
            </div>

            <div className={`text-sm font-medium ${remaining >= 0 ? 'text-green-600' : 'text-red-650'}`}>
              {remaining >= 0 ? `€${remaining} remaining` : `€${Math.abs(remaining)} over limit`}
            </div>
          </div>
        );
      })}
    </div>
  );
};
