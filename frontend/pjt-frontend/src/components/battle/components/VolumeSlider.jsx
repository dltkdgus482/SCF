import React from 'react';
import Slider from '@mui/material/Slider';

const VolumeSlider = ({ volume, handler }) => {
  const handleChange = (event, newValue) => {
    handler(newValue/100);
    console.log(volume)
  };

  return (
    <Slider
      value={typeof volume === 'number' ? volume : 0}
      onChange={handleChange}
      aria-labelledby="continuous-slider"
    />
  );
};

export default VolumeSlider;
