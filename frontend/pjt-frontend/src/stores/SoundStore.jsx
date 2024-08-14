import { create } from 'zustand';
import { Howl } from 'howler';

const SoundStore = create((set) => ({
  backgroundMusic: new Howl({
    src: ['/sounds/main-bgm.mp3'],
    loop: true,
    volume: 0.5,
  }),
  isPlayingBgm: false,
  bgmVolume: 0.5,
  effectVolume: 0.5,
  effectSounds: {
    clickSound: new Howl({
      src: ['/sounds/btn-click.mp3'], // 효과음 파일 경로
      volume: 0.5,
    }),
    singleClickSound : new Howl({
      src: ['/sounds/single-click.mp3'], // 효과음 파일 경로
      volume: 0.5,
    }),
    btnClickSound : new Howl({
      src: ['/sounds/btn-click.mp3'], // 효과음 파일 경로
      volume: 0.5,
    }),
    mainStartSound : new Howl({
      src: ['/sounds/main-start.mp3'], // 효과음 파일 경로
      volume: 0.5,
    }),
    hoverSound : new Howl({
      src : ['/sounds/hover-sound.mp3'],
      volume : 0.5,
    }), 
  },
  playBackgroundMusic: () => {
    set((state) => {
      if (state.backgroundMusic && !state.isPlaying) {
        state.backgroundMusic.play();
        return { isPlaying: true };
      }
      return {};
    });
  },
  stopBackgroundMusic: () => {
    set((state) => {
      console.log(state.isPlaying);
      if (state.backgroundMusic && state.isPlaying) {
        state.backgroundMusic.stop();
        console.log('stop background music');
        return { isPlaying: false };
      }
      return {};
    });
  },
  switchBackgroundMusic: (bgmType, callback) => {
    set((state) => {
      state.backgroundMusic.stop();
      const newBackgroundMusic = new Howl({
        src: [`/sounds/${bgmType}-bgm.mp3`],
        loop: true,
        volume: state.bgmVolume,
      });
      callback(newBackgroundMusic);
      return { backgroundMusic: newBackgroundMusic };
    });
  },
  playEffectSound: (effectType) => {
    set((state) => {
      state.effectSounds?.[effectType].play();
      return {};
    });
  },
  setBgmVolume: (volume) => {
    set((state) => {
      state.backgroundMusic.volume(volume);
      return { bgmVolume: volume };
    });
  },
  setEffectVolume: (volume) => set((state) =>{
    Object.values(state.effectSounds).forEach((sound) => sound.volume(volume));
    return { effectVolume: volume };
  })
}));

export default SoundStore;
