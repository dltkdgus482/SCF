import { create } from 'zustand';

const testRanking = {
  total: [
    {
      userId: '123',
      name: 'x탱글대기2',
      exp: 1234,
    },
    {
      userId: '123',
      name: 'x탱글대기2',
      exp: 1234,
    },
  ],
  weekly: [
    {
      userId: '123',
      name: 'x탱글대기2',
      exp: 1234,
    },
    {
      userId: '123',
      name: 'x탱글대기2',
      exp: 1234,
    },
    {
      userId: '123',
      name: 'x탱글대기2',
      exp: 1234,
    },
  ],
  daily: [
    {
      userId: '123',
      name: 'x탱글대기2',
      exp: 1234,
    },
    {
      userId: '123',
      name: 'x탱글대기2',
      exp: 1234,
    },
    {
      userId: '123',
      name: 'x탱글대기2',
      exp: 1234,
    },
  ],
};

const useLeaderboardStore = create((set) => ({
  rankingList: { total: [], weekly: [], daily: [] },
  setTotalList: (newList) =>
    set((state) => {
      console.log('total set', newList);
      console.log(state)

      return { rankingList: { ...state.rankingList, total: newList } };
    }),
  setWeeklyList: (newList) =>
    set((state) => {
      console.log('weekly set', newList);
      console.log(state)

      return { rankingList: { ...state.rankingList, weekly: newList } };
    }),
  setDailyList: (newList) =>
    set((state) => {
      console.log('daily set', newList);
      console.log(state)
      return { rankingList: { ...state.rankingList, daily: newList } };
    }),


  boardPeriod: 'total',
  setBoardPeriod: (period) => set({ boardPeriod: period }),
}));

export default useLeaderboardStore;
