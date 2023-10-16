const baseTableStyleOptions = () => {
    return {
      setCellProps: () => {
        return {
          style: {
            padding: 8,
          },
        };
      },
      setCellHeaderProps: () => {
        return {
          style: {
            padding: 8,
          },
        };
      },
    };
  };
  
export default baseTableStyleOptions;
  