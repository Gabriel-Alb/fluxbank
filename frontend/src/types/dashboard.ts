export interface DashboardBalance {
  total: number
  monthlyGain: number
  selectedPeriod: string
}

export interface MonthlyEvolutionItem {
  month: string
  value: number
  active?: boolean
}

export interface ExpenseCategory {
  id: string
  name: string
  amount: number
  percentage: number
}

export interface ExpenseOverviewData {
  monthLabel: string
  total: number
  categories: ExpenseCategory[]
}

export interface DashboardCardData {
  name: string
  variant: string
  holder: string
  maskedNumber: string
  expiresAt: string
  limitUsedPercentage: number
}

export interface QuickContact {
  id: string
  name: string
  initials: string
}

export type TransactionType = 'income' | 'expense'

export interface RecentTransaction {
  id: string
  name: string
  category: string
  amount: number
  time: string
  type: TransactionType
  iconLabel: string
}

export interface DashboardMock {
  balance: DashboardBalance
  monthlyEvolution: MonthlyEvolutionItem[]
  expenses: ExpenseOverviewData
  card: DashboardCardData
  quickContacts: QuickContact[]
  recentTransactions: RecentTransaction[]
}