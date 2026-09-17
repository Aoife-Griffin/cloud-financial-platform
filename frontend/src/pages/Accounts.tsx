export const Accounts = () => {
  const accounts = [
    { name: 'Current Account', balance: 2340 },
    { name: 'Savings', balance: 5200 },
    { name: 'Credit Card', balance: -320 },
  ];

  return (
    <div className="space-y-6 max-w-2xl">
      <h2 className="text-3xl font-bold text-gray-900">Accounts</h2>
      <div className="space-y-4">
        {accounts.map((acc, idx) => (
          <div key={idx} className="flex justify-between items-center bg-white p-6 shadow rounded-lg">
            <span className="text-lg font-medium text-gray-700">{acc.name}</span>
            <span className={`text-xl font-bold ${acc.balance >= 0 ? 'text-gray-900' : 'text-red-600'}`}>
              {acc.balance >= 0 ? `€${acc.balance}` : `-€${Math.abs(acc.balance)}`}
            </span>
          </div>
        ))}
      </div>
    </div>
  );
};
