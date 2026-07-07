import type { DashboardMock } from '../types/dashboard'

export const dashboardMock: DashboardMock = {
  balance: {
    total: 24194,
    monthlyGain: 750,
    selectedPeriod: 'Mensal',
  },

  monthlyEvolution: [
    { month: 'Jan', value: 42 },
    { month: 'Fev', value: 68 },
    { month: 'Mar', value: 31 },
    { month: 'Abr', value: 51 },
    { month: 'Mai', value: 38 },
    { month: 'Jun', value: 59 },
    { month: 'Jul', value: 54 },
    { month: 'Ago', value: 62 },
    { month: 'Set', value: 42 },
    { month: 'Out', value: 53, active: true },
    { month: 'Nov', value: 68 },
    { month: 'Dez', value: 31 },
  ],

  expenses: {
    monthLabel: 'outubro',
    total: 5774,
    categories: [
      {
        id: 'monthly-subscriptions',
        name: 'Assinaturas mensais',
        amount: 640,
        percentage: 28,
      },
      {
        id: 'grocery',
        name: 'Mercado',
        amount: 1840,
        percentage: 42,
      },
      {
        id: 'remaining-budget',
        name: 'Orçamento restante',
        amount: 3294,
        percentage: 30,
      },
    ],
  },

  card: {
    name: 'FluxBank Black',
    variant: 'Digital Banking',
    holder: 'Gabriel A. Silva',
    maskedNumber: '**** 8283',
    expiresAt: '12/29',
    limitUsedPercentage: 42,
  },

  quickContacts: [
    {
      id: 'ana',
      name: 'Ana',
      initials: 'AN',
    },
    {
      id: 'lucas',
      name: 'Lucas',
      initials: 'LU',
    },
    {
      id: 'marina',
      name: 'Marina',
      initials: 'MA',
    },
    {
      id: 'rafael',
      name: 'Rafael',
      initials: 'RA',
    },
  ],

  recentTransactions: [
    {
      id: 'amazon',
      name: 'Amazon',
      category: 'Compra',
      amount: -824.5,
      time: '3 horas atrás',
      type: 'expense',
      iconLabel: 'AM',
    },
    {
      id: 'spotify',
      name: 'Spotify',
      category: 'Assinatura',
      amount: -15,
      time: 'Hoje',
      type: 'expense',
      iconLabel: 'SP',
    },
    {
      id: 'salary',
      name: 'Salário',
      category: 'Recebimento',
      amount: 7200,
      time: 'Ontem',
      type: 'income',
      iconLabel: 'SL',
    },
    {
      id: 'electricity',
      name: 'Energia',
      category: 'Conta mensal',
      amount: -213.9,
      time: '2 dias atrás',
      type: 'expense',
      iconLabel: 'EN',
    },
  ],
}