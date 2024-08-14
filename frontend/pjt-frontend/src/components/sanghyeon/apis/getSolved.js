import axios from "axios";

function getSolved(userId) {
  axios({
    method: "GET",
    url: `/profile/solved/${userId}`,
    data: {
      userId,
    },
  })
    .then((response) => {
      console.log(response);
    })
    .catch((error) => {
      console.log(error);
    });
}

export default getSolved;
