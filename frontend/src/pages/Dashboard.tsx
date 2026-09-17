{/* Metric grid and categories being listed out. Tracking that's dynamic */}

export const Dashboard = () => {
  const cards = [
    { title: 'Balance', amount: '€5,240', color: 'text-gray-900' },
    { title: 'Income', amount: '€2,800', color: 'text-green-600' },
    { title: 'Expenses', amount: '€1,420', color: 'text-red-600' },
  ];

  const expenses = [
    { category: 'Food', amount: '€320' },
    { category: 'Transport', amount: '€180' },
    { category: 'Entertainment', amount: '€95' },
  ];

  return (
    <div className="space-y-8">
      <h2 className="text-3xl font-bold text-gray-900">Financial Dashboard</h2>
      
      {/* Metric Cards */}
      <div className="grid grid-cols-1 gap-5 sm:grid-cols-3">
        {cards.map((card, idx) => (
          <div key={idx} className="bg-white shadow rounded-lg p-6">
            <dt className="text-sm font-medium text-gray-500">{card.title}</dt>
            <dd className={`mt-1 text-3xl font-semibold ${card.color}`}>{card.amount}</dd>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        {/* Shows the list of categories */}
        <div className="bg-white p-6 shadow rounded-lg">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Spending this month</h3>
          <div className="divide-y divide-gray-200">
            {expenses.map((exp, idx) => (
              <div key={idx} className="flex justify-between py-3">
                <span className="text-gray-600">{exp.category}</span>
                <span className="font-semibold text-gray-900">{exp.amount}</span>
              </div>
            ))}
          </div>
        </div>


        {/* Gets the  */}
        <div className="bg-white p-6 shadow rounded-lg flex flex-col justify-center">
          <h3 className="text-lg font-medium text-gray-900 mb-2">Budget usage</h3>
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-gray-500">Global Limit</span>
            <span className="text-sm font-bold text-gray-900">81%</span>
          </div>
          <div className="w-full bg-gray-200 rounded-full h-4">
            <div className="bg-amber-500 h-4 rounded-full" style={{ width: '81%' }}></div>
          </div>
        </div>
      </div>
    </div>
  );
};
