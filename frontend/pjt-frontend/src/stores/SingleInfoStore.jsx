import { create } from 'zustand';

const courses = [
  { id: 1, content_type: '변수와 자료형', title: '변수' },
  { id: 2, content_type: '변수와 자료형', title: '자료형' },
  { id: 3, content_type: '연산자', title: '연산자' },
  { id: 4, content_type: '입출력', title: '표준입출력' },
  { id: 5, content_type: '입출력', title: '파일입출력' },
  { id: 6, content_type: '제어구조', title: '반복문' },
  { id: 7, content_type: '제어구조', title: '조건문' },
  { id: 8, content_type: '자료구조', title: '1차원 리스트' },
  { id: 9, content_type: '자료구조', title: '2차원 리스트' },
  { id: 10, content_type: '함수', title: '함수의 활용' },
  { id: 11, content_type: '알고리즘', title: '탐색' },
  { id: 12, content_type: '알고리즘', title: '정렬' },
]

const completed = {
  contentList: [
    {
      contentId: 1,
      complete: 1, // 수강 완료
    },
    {
      contentId: 2,
      complete: 0, // 수강 완료
    },
    {
      contentId: 3,
      complete: 0, // 수강 완료
    },
    {
      contentId: 4,
      complete: 0, // 수강 완료
    },
    {
      contentId: 5,
      complete: 0, // 수강 완료
    },
    {
      contentId: 6,
      complete: 0, // 수강 완료
    },
    {
      contentId: 7,
      complete: 0, // 수강 완료
    },
    {
      contentId: 8,
      complete: 0, // 수강 완료
    },
    {
      contentId: 9,
      complete: 0, // 수강 완료
    },
    {
      contentId: 10,
      complete: 0, // 수강 완료
    },
    {
      contentId: 11,
      complete: 0, // 수강 완료
    },
    {
      contentId: 12,
      complete: 0, // 수강 완료
    },
  ],
};

const useLeaderboardStore = create((set) => ({
  courses: courses,
  completed: completed.contentList,
  setCompleted: (completedList) => set({ completed: completedList }),
}));

export default useLeaderboardStore;
