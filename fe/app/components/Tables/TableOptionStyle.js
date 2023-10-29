const baseTableStyleOptions = (params) => {
    return {
      setCellProps: () => {
        return {
          style: {
            padding: 8,
            ...params
          },
        };
      },
      setCellHeaderProps: () => {
        return {
          style: {
            padding: 8,
            ...params
          },
        };
      },
    };
  };
  
export default baseTableStyleOptions;
  