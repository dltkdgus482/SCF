import SingleInfoStore from "../../stores/SingleInfoStore"

const checkIsCompleted = (contentNum) =>{
  const {completed} = SingleInfoStore()
  const isCompleted = completed.find((e)=>{
    return e.contentId === contentNum
  }).complete
  return isCompleted
}

export default checkIsCompleted