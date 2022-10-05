import { render, screen } from '@testing-library/react';
import Root from './App';

test('renders learn react link', () => {
  render(<Root />);
  const linkElement = screen.getByText(/Lingu/i);
  expect(linkElement).toBeInTheDocument();
});
