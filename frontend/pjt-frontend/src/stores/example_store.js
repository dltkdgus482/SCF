import create from 'zustand';

const useMemosStore = create((set) => ({
  memo: '',
  setMemo: (text) => set({ memo: text }),
  memos: [],
  setMemos: (newMemo) =>
    set((prev) => ({
      memos: [...prev.memos, newMemo],
    })),
}));

export default useMemosStore;

// 사용할 때
// ./App.js
// function App() {
//   return (
//     <div>
//       <h1>메모 작성하기</h1>
//       <Form />
//       <Memos />
//     </div>
//   );
// }
// ./component/Form.js
// import useMemosStore from '../stores/memos';

// const Form = () => {
//   const { memo, setMemo, setMemos } = useMemosStore();

//   const handleWriteMemo = (e) => {
//     setMemo(e.target.value);
//   };

//   const handleAddMemo = (e) => {
//     e.preventDefault();
//     setMemos(memo);
//     setMemo('');
//   };

//   return (
//     <>
//       <form onSubmit={handleAddMemo}>
//         <input type='text' onChange={handleWriteMemo} value={memo} />
//         <button type='submit'>작성완료</button>
//       </form>
//     </>
//   );
// };