
import { describe, it, expect } from 'vitest'; 
import { render, screen } from '@testing-library/react';
import { Dashboard } from '../Dashboard';
import '@testing-library/jest-dom/vitest';



describe('Dashboard Component  Tests', () => {
  it('should render balance, income, and absolute monthly spending summary figures', () => {
    render(<Dashboard />);
    
    expect(screen.getByText('Financial Dashboard')).toBeInTheDocument();
    expect(screen.getByText('€5,240')).toBeInTheDocument();
    expect(screen.getByText('€2,800')).toBeInTheDocument();
    expect(screen.getByText('€1,420')).toBeInTheDocument();
  });

  it('should render the budget metric progress bar text', () => {
    render(<Dashboard />);
    expect(screen.getByText('81%')).toBeInTheDocument();
  });
});
